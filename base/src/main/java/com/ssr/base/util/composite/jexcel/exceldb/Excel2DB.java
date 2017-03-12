package com.ssr.base.util.composite.jexcel.exceldb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.ssr.base.util.composite.jexcel.ConnectionSupervise;
import com.ssr.base.util.composite.jexcel.DBTypeConvertor;
import com.ssr.base.util.composite.jexcel.exceldb.sqlparser.ExcelSqlParser;

/**
 * 将excel读出，并存储进入数据库
 * 
 */
public class Excel2DB {

	private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Excel2DB.class);

	private javax.servlet.ServletContext servletContext = null;

	// 配置管理对象
	ExcelConfigManager configManager = new ExcelConfigManager();

	// 记录数据内存对象
	ExcelCacheManager cacheManager = new ExcelCacheManager();

	// 代码表反转对象。
	InverCodeListManager iCodeManager = new InverCodeListManager();

	// 传入参数列表：如：name1=value1,name2=value2
	Map<String, Object> paramMap = new HashMap<String, Object>();

	// 合并行列的缓存
	MarginCache marginCache = null;

	// 数据库联接，使用大事务，全部使用同一个连接
	Connection conn = null;
	// 设置全局的返回信息
	String exceptionMsg = "";
	// 设置提交成功计数器
	int commitCount = 0;
	// 提交计数器
	int count = 0;
	// 提交异常计数器
	int exceptionCount = 0;
	// //行增量
	// public int rowIncrement = 0;

	public Excel2DB(javax.servlet.ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	/**
	 * 读取xml配置文档
	 * 
	 * @param confName
	 *            String
	 * @param context
	 *            ServletContext
	 * @throws DocumentException
	 * @return Document
	 */
	private Document readXmlConfiguration(String confPathName) throws DocumentException {
		java.io.InputStream is = servletContext.getResourceAsStream(confPathName);
		SAXReader reader = new SAXReader();
		return reader.read(is);
	}

	/**
	 * excel保存进入数据库
	 * 
	 * @param excelInStream
	 *            InputStream
	 * @param confName
	 *            String
	 * @param context
	 *            ServletContext
	 * @return String
	 */
	public String excel2DB(java.io.InputStream excelInStream, String confPathName) throws Exception {
		return excel2DB(excelInStream, confPathName, null);
	}

	/**
	 * excel保存进入数据库
	 * 
	 * @param excelInStream
	 *            InputStream
	 * @param confName
	 *            String
	 * @param context
	 *            ServletContext
	 * @return String
	 */
	public String excel2DB(java.io.InputStream excelInStream, String confPathName, String paramListStr)
			throws Exception {
		Document doc = null;
		try {
			// 读取配置文件
			doc = readXmlConfiguration(confPathName);
		} catch (DocumentException ex) {
			throw new Exception("读取配置文件" + confPathName + "失败！没有导入任何数据！", ex);
		}

		// 解析参数字符串
		parseParamListStr(paramListStr);

		// 获取根节点
		Element root = doc.getRootElement();

		// 解析配置文件
		parseConfiguration(root);

		// 获取数据库连接
		ConnectionSupervise connSup = ConnectionSuperviseFactory
				.getConnectionSupervise(configManager.getConnectionSuperviseClass());
		try {
			conn = connSup.getConnection();
		} catch (Exception ex1) {
			throw new Exception("获取连接错误:" + ex1.getMessage(), ex1);
		}

		boolean commitStore = true;
		try {
			// 使用大事务,使用自动提交,使得数据可以部分成功。
			commitStore = conn.getAutoCommit();
			conn.setAutoCommit(true);
		} catch (SQLException ex2) {
			throw new Exception("启动手动提交事务错误:" + ex2.getMessage(), ex2);
		}
		HSSFWorkbook book = null;
		try {
			/**
			 * 循环Excel sheet,将每个sheet入库
			 */
			System.out.println("开始读取Excel");
			book = new HSSFWorkbook(excelInStream);
			System.out.println("结束读取Excel");
			int sheetcount = book.getNumberOfSheets();
			for (int i = 0; i < sheetcount; i++) {
				HSSFSheet sheet = book.getSheetAt(i);
				String sheetName = book.getSheetName(i);

				// 获取sheet的配置
				ExcelSheetConfig esc = getSheetConfig(sheetName);

				// 如果Excel中Sheet与配置文件名称不能够匹配且ExcelSheet个数只有一个,那么将配置文件中的第一个Sheet配置取出来进行匹配数据
				if (sheetcount == 1) {
					esc = configManager.getFirstExcelSheetConfig();
					// configManager.getExcelSheetConfig(sheetName);
				} else if (esc == null) {
					// throw new Exception ("表单["+sheetName+"]的配置不存在！");
					log.info("\n上传excel入库，表单[" + sheetName + "]的配置不存在！");
					continue;
				}

				System.out.println("读取当前sheet:" + esc.getSheetName());

				// 创建一个新的cache存储当前sheet的数据
				cacheManager.newSheetCache();

				// 读取location数据
				readLocationData(esc, book, sheet);

				// 创建合并行列缓存
				marginCache = new MarginCache(sheet);

				// 循环sheet的根节点配置，并入库
				for (Iterator<ExcelFLConfig> it = esc.getFLRootIterator(); it.hasNext();) {
					ExcelFLConfig flc = (ExcelFLConfig) it.next();
					recursiveProcess(flc, sheet, -1, -1);
				}
			}

			// 清除内存
			cacheManager.clearCache();

			/*
			 * 修改为自动提交 conn.commit();
			 */
		} catch (Exception ex) {
			try {
				// 出现错误，回滚事务
				// conn.rollback();
			} catch (Exception e) {

			}

			// 抛出错误
			if (ex instanceof Exception) {
				throw (Exception) ex;
			} else {
				ex.printStackTrace();
				throw new Exception(ex.getMessage(), ex);
			}
		} finally {
			try {
				log.info("数据入库情况:" + this.exceptionMsg);
				// 恢复事务提交方式
				conn.setAutoCommit(commitStore);
			} catch (Exception e) {
			}
			try {
				// 关闭连接
				connSup.closeConnection(conn);
			} catch (Exception e2) {
			}
		}
		this.exceptionCount = this.count - this.commitCount;
		return this.count + ":" + this.commitCount + ":" + this.exceptionCount;
	}

	/**
	 * 解析传入的参数，参数使用逗号分隔开
	 * 
	 * @param paramListStr
	 *            String
	 */
	private void parseParamListStr(String paramListStr) throws Exception {

		if (paramListStr != null) {
			String segments[] = paramListStr.split(",");
			for (int i = 0; i < segments.length; i++) {
				String s[] = segments[i].split("=");
				if (s.length != 2) {
					throw new Exception("传入参数按逗号分割后的字符串段“" + segments[i] + "”不合法！");
				}

				paramMap.put(s[0], s[1]);
			}
		}
	}

	/**
	 * 读取一个sheet的locations进入内存中
	 * 
	 * @param esc
	 *            ExcelSheetConfig
	 * @param sheet
	 *            HSSFSheet
	 * @throws Exception
	 */
	private void readLocationData(ExcelSheetConfig esc, HSSFWorkbook book, HSSFSheet sheet) throws Exception {
		for (Iterator<LocationConfig> it = esc.getLocationIterator(); it.hasNext();) {
			LocationConfig lc = (LocationConfig) it.next();
			HSSFSheet useSheet = sheet;
			if (lc.getSheetName() != null && lc.getSheetName().trim().length() != 0) {
				// 如果sheetName不等于空，则从其他sheet取值
				useSheet = book.getSheet(lc.getSheetName());
			}

			// 搜索基准行
			int baseRow = searchString(useSheet, lc.getStartRow(), lc.getStartCol(), lc.getStartStr());
			int rowIndex = baseRow + lc.getRelativeX();
			// 取值
			Object value = ExcelHelper.getCellObjectValue(useSheet, rowIndex, lc.getAbsCol(), lc.getType(),
					lc.getFormat(), lc.getCodeType(), servletContext);

			cacheManager.putLocation(lc.getId(), value);
		}
	}

	/**
	 * 递归根据配置入库，
	 * 
	 * @param flc
	 *            ExcelFLConfig excel的form 或者loop 配置
	 * @param sheet
	 *            HSSFSheet sheet对象
	 * @param beginRow
	 *            int 起始读取，或者搜索行，如果该参数不为-1，则以该参数为准，否则以配置为准
	 * @param endRow
	 *            int 结束读取行，如果该参数不为-1，则以该参数为准，否则以空行或者配置的结束字符串为准
	 * @throws Exception
	 */
	private void recursiveProcess(ExcelFLConfig flc, HSSFSheet sheet, int beginRow, int endRow) throws Exception {
		if (flc.isForm()) {
			processForm(flc, sheet, beginRow, endRow);
		} else if (!flc.isLoopContenter()) {
			processLoop(flc, sheet, beginRow, endRow);
		} else {
			processLoopContenter(flc, sheet, beginRow, endRow);
		}
	}

	/**
	 * 将form类型入库
	 * 
	 * @param flc
	 *            ExcelFLConfig
	 * @param sheet
	 *            HSSFSheet
	 * @param beginRow
	 *            int 起始读取，或者搜索行，如果该参数不为-1，则以该参数为准，否则以配置为准
	 * @param endRow
	 *            int 结束读取行，如果该参数不为-1，则以该参数为准，否则以空行或者配置的结束字符串为准
	 * @throws Exception
	 */
	private void processForm(ExcelFLConfig flc, HSSFSheet sheet, int beginRow, int endRow) throws Exception {

		int startRow = beginRow;
		if (beginRow < 0) {
			// 如果没有指定查找行，则从配置中获取。
			startRow = flc.getStartRow();
		}

		// 读取单元格的基准行
		int baseRowIndex = -1;
		baseRowIndex = searchString(sheet, startRow, flc.getStartCol(), flc.getStartStr());

		// sql的序号，第几条sql
		int sqlIndex = 0;
		/**
		 * 循环处理每一个sql语句。
		 */
		for (Iterator<ExcelSql> it = flc.getExcelSqlIterator(); it.hasNext(); sqlIndex++) {
			/*
			 * 记录代入库数据量
			 */
			this.count = this.count + 1;

			ExcelSql excelSql = (ExcelSql) it.next();
			java.sql.PreparedStatement pstmt = null;
			java.sql.CallableStatement cstmt = null;
			try {
				if (flc.getOrgiSqls() != null) {
					pstmt = conn.prepareStatement(excelSql.getParsedSql());
				} else {
					cstmt = conn.prepareCall(excelSql.getParsedSql());
				}
			} catch (SQLException ex) {
				/*
				 * 处理掉Exception,继续处理后续的数据入库 throw new
				 * Exception(ex.getMessage(), ex);
				 */
				this.exceptionMsg = this.exceptionMsg + "创建prepareStatement或prepareCall出错:[" + ex.getMessage() + "]\n";
				continue;
			}
			// 创建form的新缓存
			cacheManager.newRecord(flc.getId());
			// 值插入预编译对象的位置
			int position = 1;
			// 循环读取字段
			for (Iterator<ExcelSqlField> fit = excelSql.getFieldsIterator(); fit.hasNext(); position++) {
				ExcelSqlField field = (ExcelSqlField) fit.next();
				Object value;
				// 根据字段读取一个form单元格的值。
				try {
					value = readFormFieldValue(flc.getId(), sheet, baseRowIndex, field);
				} catch (Exception vex) {
					this.exceptionMsg = this.exceptionMsg + "ExcelToDb类型转换出错:[" + vex.getMessage() + "]\n";
					continue;
				}

				// 设置值进入pstmt
				try {
					if (flc.getOrgiSqls() != null) {
						if (value == null) {
							pstmt.setNull(position, field.getFieldType());
						} else {
							pstmt.setObject(position, value);
						}
					} else {
						if (value == null) {
							cstmt.setNull(position, field.getFieldType());
						} else {
							cstmt.setObject(position, value);
						}
					}
				} catch (SQLException ex2) {
					/*
					 * 处理掉Exception,继续处理后续的数据入库 throw new
					 * Exception(ex2.getMessage(), ex2);
					 */
					this.exceptionMsg = this.exceptionMsg + "插入值{+" + value + "}出错:[" + ex2.getMessage() + "]\n";
					continue;
				}

				if (sqlIndex == 0 && flc.getOrgiSqls() != null) {
					// 保存值到缓存，行记录id,字段名,值
					cacheManager.putField(flc.getId(), field.getFieldName(), value);
				}
			}

			if (!flc.isJustMemory()) {
				// 如果不是只读入到内存中，择要入库
				try {
					// 执行新增
					if (flc.getOrgiSqls() != null) {
						pstmt.executeUpdate();
						this.commitCount = this.commitCount + 1;
					} else {
						cstmt.execute();
						this.commitCount = this.commitCount + 1;
					}
				} catch (SQLException ex1) {
					/*
					 * 处理掉Exception,继续处理后续的数据入库 throw new
					 * Exception("新增入库sql:" + excelSql.getOrgSql() + "错误：" +
					 * ex1.getMessage(), ex1);
					 */
					this.exceptionMsg = this.exceptionMsg + "新增入库sql{" + excelSql.getOrgSql() + "}出错:["
							+ ex1.getMessage() + "]\n";
					continue;
				} finally {
					try {
						if (flc.getOrgiSqls() != null) {
							pstmt.close();
						} else {
							cstmt.close();
						}
					} catch (Exception e) {
					}
				}
			}
		}

		for (Iterator<ExcelFLConfig> it = flc.getFLChildrenIterator(); it.hasNext();) {
			// 循环根据子配置继续入库
			ExcelFLConfig flcChild = (ExcelFLConfig) it.next();
			recursiveProcess(flcChild, sheet, baseRowIndex, -1);
		}
	}

	/**
	 * 根据字段读取一个form单元格的值。
	 * 
	 * @param formConfId
	 *            String
	 * @param sheet
	 *            HSSFSheet
	 * @param baseRowIndex
	 *            int
	 * @param field
	 *            ExcelSqlField
	 * @throws Exception
	 * @return Object
	 */
	private Object readFormFieldValue(String formConfId, HSSFSheet sheet, int baseRowIndex, ExcelSqlField field)
			throws Exception {
		Object value = null;
		switch (field.getRetrieveTyp()) {
		case ExcelSqlField.SEQUENCES:
			value = getSequences(field.getSeqName());
			// 调用seq
			break;
		case ExcelSqlField.LOCATION_MOMORY:
			value = cacheManager.getLocation(field.getId());
			break;
		case ExcelSqlField.FORM_MEMORY:
			value = cacheManager.getFields(field.getId(), field.getMemoryFieldName());
			break;
		case ExcelSqlField.LOOP_MEMORY:
			value = cacheManager.getFields(field.getId(), field.getMemoryFieldName());
			break;
		case ExcelSqlField.PARAMETER:
			value = paramMap.get(field.getMemoryFieldName());
			value = ExcelHelper.str2sqlValue((String) value, field.getFieldType(), field.getFormat(),
					field.getCodeTypeName(), servletContext);
			break;
		case ExcelSqlField.LOOP_EXCEL:
			throw new Exception("id为[" + formConfId + "]的form配置中不能拥有loop的excel单元格读取方式");
		case ExcelSqlField.FORM_EXCEL:
			value = ExcelHelper.getCellObjectValue(sheet, baseRowIndex + field.getRelativeX(), field.getAbsCol(),
					field.getFieldType(), field.getFormat(), field.getCodeTypeName(), servletContext);
			break;
		}
		return value;
	}

	/**
	 * 搜索定位字符串
	 * 
	 * @param sheet
	 *            HSSFSheet
	 * @param startRow
	 *            int 起始查找行
	 * @param col
	 *            short 查找列
	 * @param searchStr
	 *            String 查找的字符串
	 */
	private int searchString(HSSFSheet sheet, int startRow, short col, String searchStr) throws Exception {
		// 记录连续搜索到的空白字符串
		int stop = 0;
		int movedRow = 0;
		for (; stop < 20; stop++, movedRow++) {
			HSSFRow row = sheet.getRow(startRow + movedRow);
			if (row == null) {
				continue;
			}
			// 如果不为空行,空行记录置为零
			stop = 0;
			Object objValue = ExcelHelper.getCellObjectValue(sheet, startRow, col, java.sql.Types.VARCHAR, null, null,
					null);
			// System.out.println("objValue==============================="+objValue);
			if (objValue == null) {
				continue;
			}
			String value = objValue.toString();
			if (value.trim().toUpperCase().equals(searchStr.toUpperCase())) {
				// 搜索到匹配的字符串
				return startRow + movedRow;
			}
		}

		throw new Exception("要搜索定位的字符串[" + searchStr + "]在第[" + col + "]列不存在！");
	}

	/**
	 * 将loop类型类型配置入库
	 * 
	 * @param flc
	 *            ExcelFLConfig
	 * @param sheet
	 *            HSSFSheet
	 * @param beginRow
	 *            int 起始读取，或者搜索行，如果该参数不为-1，则以该参数为准，否则以配置为准
	 * @param endRow
	 *            int 结束读取行，如果该参数不为-1，则以该参数为准，否则以空行或者配置的结束字符串为准
	 * @throws Exception
	 */
	private void processLoop(ExcelFLConfig flc, HSSFSheet sheet, int beginRow, int endRow) throws Exception {

		int startRow = beginRow;
		if (beginRow < 0) {
			// 如果没有指定查找行，则从配置中获取。
			startRow = flc.getStartRow();
		}

		/**
		 * 读取单元格的基准行
		 */
		int baseRowIndex = -1;

		if (flc.isRoot() || flc.isParentForm()) {
			// 如果是根，或者父配置是form，则需要搜索定位基准行
			baseRowIndex = searchString(sheet, startRow, flc.getStartCol(), flc.getStartStr());
			// 搜索到之后的下一行为基本行，及开始读入数据行
			baseRowIndex++;
		} else {
			// 如果父配置是loop，则起始行就是基准行
			baseRowIndex = startRow;
		}

		/**
		 * 循环读取行，保存行
		 */
		for (int rowIndex = baseRowIndex;;) {
			// 为行记录创建新缓存
			cacheManager.newRecord(flc.getId());

			HSSFRow row = sheet.getRow(rowIndex);
			if (row == null) {
				// 如果为空行，返回，结束
				return;
			}

			if (endRow <= 0 && hasEndStr(row, flc.getEndCol(), flc.getEndstr())) {
				// 如果只定了结束行且该行含有结束字符串，结束读取，返回
				return;
			}

			// 用来记录，指向下一条数据时需要移动多少行
			int movePoint = -1;
			// 创建预编译对象
			java.sql.PreparedStatement pstmt = null;
			// 创建存储过程执行对象
			java.sql.CallableStatement cstmt = null;
			// sql的顺序号，
			int sqlIndex = 0;
			/**
			 * 循环处理每一条sql语句
			 */
			for (Iterator<ExcelSql> it = flc.getExcelSqlIterator(); it.hasNext(); sqlIndex++) {
				/*
				 * 记录代入库数据量
				 */
				this.count = this.count + 1;
				ExcelSql excelSql = (ExcelSql) it.next();

				try {

					if (flc.getOrgiSqls() != null) {

						pstmt = conn.prepareStatement(excelSql.getParsedSql());
					} else {
						cstmt = conn.prepareCall(excelSql.getParsedSql());
					}
				} catch (SQLException ex) {
					/*
					 * 处理掉Exception,继续处理后续的数据入库 throw new
					 * Exception(ex.getMessage(), ex);
					 */
					this.exceptionMsg = this.exceptionMsg + "创建prepareStatement或prepareCall出错:[" + ex.getMessage()
							+ "]\n";
					continue;
				}
				// 值插入预编译对象的位置
				int position = 1;
				// isEmptyRow，用来判断该行是否为空行。
				boolean isEmptyRow = true;
				// 当前sql语句是否读取过excel
				boolean excelReaded = false;
				// 循环读取字段
				for (Iterator<ExcelSqlField> fit = excelSql.getFieldsIterator(); fit.hasNext(); position++) {
					ExcelSqlField field = (ExcelSqlField) fit.next();
					Object value = null;
					switch (field.getRetrieveTyp()) {
					case ExcelSqlField.SEQUENCES:
						value = getSequences(field.getSeqName());
						// 调用seq
						break;
					case ExcelSqlField.LOCATION_MOMORY:
						value = cacheManager.getLocation(field.getId());
						break;
					case ExcelSqlField.FORM_MEMORY:
						value = cacheManager.getFields(field.getId(), field.getMemoryFieldName());
						break;
					case ExcelSqlField.LOOP_MEMORY:
						value = cacheManager.getFields(field.getId(), field.getMemoryFieldName());
						break;
					case ExcelSqlField.PARAMETER:
						value = paramMap.get(field.getMemoryFieldName());
						try {
							value = ExcelHelper.str2sqlValue((String) value, field.getFieldType(), field.getFormat(),
									field.getCodeTypeName(), servletContext);
							break;
						} catch (Exception vex) {
							this.exceptionMsg = this.exceptionMsg + "ExcelToDb类型转换出错:[" + vex.getMessage() + "]\n";
							continue;
						}
					case ExcelSqlField.LOOP_EXCEL:
						try {
							value = ExcelHelper.getCellObjectValue(row, field.getAbsCol(), field.getFieldType(),
									field.getFormat(), field.getCodeTypeName(), servletContext);
						} catch (Exception vex) {
							this.exceptionMsg = this.exceptionMsg + "ExcelToDb类型转换出错:[" + vex.getMessage() + "]\n";
							continue;
						}

						excelReaded = true;
						if (value != null && value.toString().trim().length() != 0) {
							// 如果不等于空，那么该行不是空行
							isEmptyRow = false;
						}

						// 计算移动到下一记录需要移动多少行
						if (movePoint == -1) {
							// 如果等于-1则没有计算过，计算一次
							int tmpMove = marginCache.getMarginRow(rowIndex, field.getAbsCol());
							if (tmpMove != -1) {
								movePoint = tmpMove;
							} else {
								// 如果没找到合并行，则为移动一行
								movePoint = 1;
							}
						}
						break;
					case ExcelSqlField.FORM_EXCEL:
						/*
						 * 处理掉Exception,继续执行数据入库 throw new Exception("id为[" +
						 * flc.getId() + "]的loop配置中不能拥有form的excel读取方式");
						 */
						this.exceptionMsg = this.exceptionMsg + "id为{" + flc.getId() + "}的loop配置中不能拥有form的excel读取方式\n";
						continue;
					}

					// 设置值进入pstmt
					try {
						if (flc.getOrgiSqls() != null) {
							// sql
							if (value == null) {
								pstmt.setNull(position, field.getFieldType());
							} else {
								pstmt.setObject(position, value);
							}
						} else {
							// 存储过程
							if (value == null) {

								cstmt.setNull(position, field.getFieldType());
							} else {

								cstmt.setObject(position, value);
							}
						}
					} catch (SQLException ex2) {
						/*
						 * 处理掉Exception,继续处理后续的数据入库 throw new
						 * Exception("插入值[" + value + "]出错：" +
						 * ex2.getMessage(), ex2);
						 */
						this.exceptionMsg = this.exceptionMsg + "插入值{+" + value + "}出错:[" + ex2.getMessage() + "]\n";
						continue;
					}

					if (sqlIndex == 0 && flc.getOrgiSqls() != null) {
						/**
						 * 只保存第一条sql的数据 保存值到缓存，行记录id,字段名,值
						 */
						cacheManager.putField(flc.getId(), field.getFieldName(), value);
					}
				}

				if (isEmptyRow == true && excelReaded == true) {
					// 该行全是空字符串，返回，结束，不执行新增。只有读过excel才知道是不是空。
					return;
				}

				if (!flc.isJustMemory()) {

					// 如果不是只读入到内存中，择要入库
					try {

						// 执行新增
						if (flc.getOrgiSqls() != null) {
							pstmt.executeUpdate();
							this.commitCount = this.commitCount + 1;
						} else {
							cstmt.execute();
							this.commitCount = this.commitCount + 1;
						}
					} catch (SQLException ex1) {
						/*
						 * 处理掉Exception,继续处理后续的数据入库 throw new
						 * Exception("新增入库sql:" + excelSql.getOrgSql() +
						 * "错误：" + ex1.getMessage(), ex1);
						 */
						this.exceptionMsg = this.exceptionMsg + "新增入库sql{" + excelSql.getOrgSql() + "}出错:["
								+ ex1.getMessage() + "]\n";
						continue;
					}

					finally {
						try {
							if (flc.getOrgiSqls() != null) {
								pstmt.close();
							} else {
								cstmt.close();
							}
						} catch (Exception e) {
						}
					}
				}
			}

			/**
			 * 循环处理每一行的子配置
			 */
			for (Iterator<ExcelFLConfig> it = flc.getFLChildrenIterator(); it.hasNext();) {
				ExcelFLConfig flcChild = (ExcelFLConfig) it.next();
				// 递归调用，设置起始终止行
				recursiveProcess(flcChild, sheet, rowIndex, rowIndex + movePoint);
			}

			/**
			 * 判断是否结束，且移动指针指向下一条数据
			 */
			rowIndex += movePoint;
			if (endRow > 0 && rowIndex >= endRow) {
				// 如果指定了结束行，并且读取行已经超出结束行，那么循环结束
				break;
			}
		}
	}

	/**
	 * 处理循环容器，该容器循环不做保存操作，只循环处理子标签
	 * 
	 * @param flc
	 *            ExcelFLConfig
	 * @param sheet
	 *            HSSFSheet
	 * @param beginRow
	 *            int
	 * @param endRow
	 *            int
	 * @throws Exception
	 */
	private void processLoopContenter(ExcelFLConfig flc, HSSFSheet sheet, int beginRow, int endRow)
			throws Exception {

		int startRow = beginRow;
		if (beginRow < 0) {
			// 如果没有指定查找行，则从配置中获取。
			startRow = flc.getStartRow();
		}

		/**
		 * 读取单元格的基准行
		 */
		int baseRowIndex = -1;
		if (flc.isRoot() || flc.isParentForm()) {
			// 如果是根，或者父配置是form，则需要搜索定位基准行
			baseRowIndex = searchString(sheet, startRow, flc.getStartCol(), flc.getStartStr());
		} else {
			// 如果父配置是loop，则起始行就是基准行
			baseRowIndex = startRow;
		}

		/**
		 * 循环读取行，保存行
		 */
		for (int rowIndex = baseRowIndex;;) {

			for (Iterator<ExcelFLConfig> it = flc.getFLChildrenIterator(); it.hasNext();) {
				ExcelFLConfig flcChild = (ExcelFLConfig) it.next();
				// 循环调用子配置，设置起始结束行，结束行使用父传进来的，如果父没有设置则没有
				recursiveProcess(flcChild, sheet, rowIndex, endRow);
			}

			// 该行已经处理了，从下一行开始循环查找下一个循环点。
			rowIndex++;
			int emptyCounter = 0;

			for (;; rowIndex++, emptyCounter++) {
				if (emptyCounter == 20) {
					// 如果连续发现20个空行，结束循环
					return;
				}

				HSSFRow row = sheet.getRow(rowIndex);
				if (row == null) {
					// 如果为空，跳过
					continue;
				}

				// 如果不为空，空行计数器置零
				emptyCounter = 0;

				if (hasEndStr(row, flc.getEndCol(), flc.getEndstr())) {
					// 如果该行含有结束字符串，结束，返回
					return;
				}

				if (endRow > 0 && rowIndex >= endRow) {
					// 如果有指定行，且超出指定行，结束，返回
					return;
				}

				Object objValue = ExcelHelper.getCellObjectValue(row, flc.getStartCol(), java.sql.Types.VARCHAR, null,
						null, null);
				if (objValue != null && objValue.toString().trim().equals(flc.getStartStr().trim())) {
					// 找到了循环容器的下一个起始位置
					break;
				}
			}
		}
	}

	/**
	 * 该行是否含有结束字符串，有：true，没有:false 单元格只以字符串读取
	 * 
	 * @param row
	 *            HSSFRow 读取的行
	 * @param endCol
	 *            short 读取的列
	 * @param endStr
	 *            String 相匹配的结束字符串
	 * @return boolean
	 */
	private boolean hasEndStr(HSSFRow row, short endCol, String endStr) throws Exception {
		boolean result = false;

		Object readEndStr = ExcelHelper.getCellObjectValue(row, endCol, java.sql.Types.VARCHAR, null, null, null);
		if (endStr != null && readEndStr != null && readEndStr.toString().trim().equals(endStr.trim())) {
			// 如果该字段等于结束字符串，那么这是结束行，返回
			result = true;
		}

		return result;
	}

	/**
	 * 获取序列号
	 * 
	 * @param seqName
	 *            String
	 * @return String
	 */
	private String getSequences(String seqName) throws Exception {
		String result = null;
		java.sql.PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement("select " + seqName + ".nextval from dual");
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getString(1);
			}
		} catch (SQLException ex) {
			throw new Exception("获取sequences错误：" + ex.getMessage(), ex);
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}

			try {
				pstmt.close();
			} catch (Exception e) {
			}
		}

		return result;

	}

	/**
	 * 将string设置进pstmt对象
	 * 
	 * @param value
	 *            String
	 * @param fieldType
	 *            int
	 * @param position
	 *            int
	 */
	public void setPstmtValue(java.sql.PreparedStatement pstmt, String value, ExcelSqlField field, int position)
			throws Exception {
		try {
			if (field.getFieldType() == java.sql.Types.DATE && field.getFormat() != null
					&& field.getFormat().trim().length() != 0) {
				// 如果是日期，有特殊格式，按照格式来转换
				java.sql.Date date = DBTypeConvertor.convertDate(value, field.getFormat());
				pstmt.setDate(position, date);
			} else {
				Object obj = DBTypeConvertor.convert(value, field.getFieldType());
				pstmt.setObject(position, obj);
			}
		} catch (SQLException ex) {
			throw new Exception(ex.getMessage(), ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception("转换值[" + value + "]的数据类型出现错误！", ex);
		}
	}

	/**
	 * 获取sheet的配置
	 * 
	 * @param sheetName
	 *            String sheet名称
	 * @throws Exception
	 *             如果没有找到对应的配置抛出异常
	 * @return ExcelSheetConfig 配置对象
	 */
	private ExcelSheetConfig getSheetConfig(String sheetName) throws Exception {
		String name = sheetName;
		int wideCharPosition = sheetName.indexOf("*");
		if (wideCharPosition != -1) {
			name = sheetName.substring(wideCharPosition);
		}

		return configManager.getExcelSheetConfig(name);
	}

	/**
	 * 检查location的属性
	 * 
	 * @param id
	 *            String
	 * @param value
	 *            String
	 * @param vName
	 *            String
	 * @throws Exception
	 */
	private void checkLocationAttr(String id, String value, String vName) throws Exception {
		if (value == null || value.trim().length() == 0) {
			throw new Exception("id为:" + id + "的location配置的" + vName + "属性为空！");
		}
	}

	/**
	 * 解析location配置
	 * 
	 * @param esc
	 *            ExcelSheetConfig
	 * @param eSheet
	 *            Element
	 * @throws Exception
	 */
	private void parseLocation(ExcelSheetConfig esc, Element eSheet) throws Exception {

		for (Iterator<Element> it = eSheet.elementIterator("location"); it.hasNext();) {
			Element eLocation = (Element) it.next();
			// 获取属性并检查是否为空
			String id = eLocation.attributeValue("id");
			if (id == null || id.trim().length() == 0) {
				throw new Exception("[" + esc.getSheetName() + "]sheet配置的location的id为空！");
			}
			String sheetName = eLocation.attributeValue("sheet");
			String startRow = eLocation.attributeValue("startRow");
			checkLocationAttr(id, startRow, "startRow");
			String startCol = eLocation.attributeValue("startCol");
			checkLocationAttr(id, startCol, "startCol");
			String startStr = eLocation.attributeValue("startStr");
			checkLocationAttr(id, startStr, "startStr");
			String relativeX = eLocation.attributeValue("relativeX");
			checkLocationAttr(id, relativeX, "relativeX");
			String absCol = eLocation.attributeValue("absCol");
			checkLocationAttr(id, absCol, "absCol");
			String type = eLocation.attributeValue("type");
			checkLocationAttr(id, type, "type");
			String format = eLocation.attributeValue("format");
			String codeType = eLocation.attributeValue("codeType");

			/**
			 * 设置属性，并检查整数是否正常
			 */
			LocationConfig lc = new LocationConfig();
			lc.setId(id);
			lc.setSheetName(sheetName);
			lc.setStartStr(startStr);
			lc.setType(DBTypeConvertor.getType(type));
			if (format != null && format.trim().length() != 0) {
				lc.setFormat(format);
			}
			if (codeType != null && codeType.trim().length() != 0) {
				lc.setCodeType(codeType);
			}

			try {
				lc.setStartRow(Integer.parseInt(startRow));
				lc.setStartCol(Short.parseShort(startCol));
				lc.setRelativeX(Integer.parseInt(relativeX));
				lc.setAbsCol(Short.parseShort(absCol));
			} catch (NumberFormatException ex) {
				throw new Exception("id为[" + id + "]的location配置的[startRow,startCol,relativeX,absCol]属性必须为数字！");
			}

			if (lc.getStartRow() < 0 || lc.getStartCol() < 0 || lc.getRelativeX() < 0 || lc.getAbsCol() < 0) {
				throw new Exception("id为[" + id + "]的location配置的[startRow,startCol,relativeX,absCol]属性必须大于等于0！");
			}

			esc.putLocation(id, lc);
		}
	}

	/**
	 * 解析form或者loop配置
	 * 
	 * @param eFL
	 *            Element form或者loop的element
	 * @param child
	 *            boolean 是否是子配置
	 */
	private void parseFLConfiguration(ExcelSheetConfig esc, Element eFL, ExcelFLConfig parent) throws Exception {
		String tagName = eFL.getName().trim().toLowerCase();
		if (!(tagName.toLowerCase().equals("form") || tagName.toLowerCase().equals("loop"))) {
			// 只解析form 和 loop
			return;
		}

		ExcelFLConfig eflc = new ExcelFLConfig();
		eflc.setParent(parent);

		// 解析读取form和loop公共配置属性
		readFLCommConf(eFL, eflc);
		// 分别解析配置
		if (tagName.toLowerCase().equals("form")) {
			parseFLFormConf(eFL, parent, eflc);
		} else {
			parseFLLoopConf(eFL, parent, eflc);
		}

		// 链接配置。将子连接到父上去,顶层连接到sheet配置中去
		if (parent == null) {
			esc.addFLConfig(eflc);
		} else {
			parent.addFLConfigChild(eflc);
		}
	}

	/**
	 * 解析form配置
	 * 
	 * @param eFL
	 *            Element
	 * @param parent
	 *            ExcelFLConfig
	 * @param eflc
	 *            ExcelFLConfig
	 * @throws Exception
	 */
	private void parseFLFormConf(Element eFL, ExcelFLConfig parent, ExcelFLConfig eflc) throws Exception {
		// 设置该配置为form配置
		eflc.setForm(true);
		// 解析startCol，并效验属性合法性
		String startCol = eFL.attributeValue("startCol");
		if (startCol == null || startCol.trim().length() == 0) {
			throw new Exception("form标签配置中必须设置startCol属性！");
		}

		if (startCol != null && startCol.trim().length() != 0) {
			try {
				eflc.setStartCol(Short.parseShort(startCol));
			} catch (NumberFormatException ex) {
				throw new Exception("id为[" + eflc.getId() + "]的标签的startCol必须为短整形(short)！");
			}

			if (eflc.getStartCol() < 0) {
				throw new Exception("id为[" + eflc.getId() + "]的标签的startCol必须大于等于0！");
			}
		}

		// 解析startStr，并效验属性合法性
		String startStr = eFL.attributeValue("startStr");
		if (startStr == null || startStr.trim().length() == 0) {
			throw new Exception("id为[" + eflc.getId() + "]的子标签必须设置startStr属性！");
		}
		eflc.setStartStr(startStr);

		// 解析sql属性，并效验属性合法性
		String sql = eFL.attributeValue("sql");
		if ((eflc.getOrgProcedure() == null || eflc.getOrgProcedure().trim().length() == 0)
				&& (sql == null || sql.trim().length() == 0)) {
			throw new Exception("id为[" + eflc.getId() + "]的子标签必须设置sql或者procedure属性！");
		} else if (eflc.getOrgProcedure() != null && eflc.getOrgProcedure().trim().length() != 0 && sql != null
				&& sql.trim().length() != 0) {
			throw new Exception("id为[" + eflc.getId() + "]的子标签不能同时设置sql或者procedure属性！");
		}

		if (sql != null && sql.trim().length() != 0) {
			eflc.setOrgiSqls(sql.trim());
		}

		/**
		 * 解析sql
		 */
		parseSql(eflc);

		for (Iterator<Element> it = eFL.elementIterator(); it.hasNext();) {
			// 递归解析子配置
			parseFLConfiguration(null, (Element) it.next(), eflc);
		}
	}

	/**
	 * 解析loop配置
	 * 
	 * @param eFL
	 *            Element
	 * @param parent
	 *            ExcelFLConfig
	 * @param eflc
	 *            ExcelFLConfig
	 * @throws Exception
	 */
	private void parseFLLoopConf(Element eFL, ExcelFLConfig parent, ExcelFLConfig eflc) throws Exception {
		// 设置该配置为form配置
		eflc.setForm(false);
		// 解析startCol，并效验属性合法性
		String startCol = eFL.attributeValue("startCol");
		if ((startCol == null || startCol.trim().length() == 0) && parent == null) {
			throw new Exception("form或者loop的顶层配置中必须设置startCol属性！");
		} else if ((startCol == null || startCol.trim().length() == 0)
				&& (parent != null && (parent.isForm() || parent.isLoopContenter()))) {
			throw new Exception("父标签是form或者循环容器,id为[" + eflc.getId() + "]的子标签必须设置startCol属性！");
		}

		if (startCol != null && startCol.trim().length() != 0) {
			try {
				eflc.setStartCol(Short.parseShort(startCol));
			} catch (NumberFormatException ex) {
				throw new Exception("id为[" + eflc.getId() + "]的标签的startCol必须为短整形(short)！");
			}

			if (eflc.getStartCol() < 0) {
				throw new Exception("id为[" + eflc.getId() + "]的标签的startCol必须大于等于0！");
			}
		}

		// 解析startStr，并效验属性合法性
		String startStr = eFL.attributeValue("startStr");
		if ((startStr == null || startStr.trim().length() == 0) && parent == null) {
			throw new Exception("form或者loop的顶层配置中必须设置startStr属性！");
		} else if ((startStr == null || startStr.trim().length() == 0)
				&& (parent != null && (parent.isForm() || parent.isLoopContenter()))) {
			throw new Exception("父标签是form或者循环容器,id为[" + eflc.getId() + "]的子标签必须设置startStr属性！");
		}
		eflc.setStartStr(startStr);

		// 解析endCol，并效验属性合法性
		String endCol = eFL.attributeValue("endCol");
		// if ( (endCol == null || endCol.trim().length() == 0) && (parent ==
		// null || parent.isForm())) {
		// throw new Exception("如果是顶层form或loop或父标签是form,id为[" + eflc.getId()
		// + "]的子标签必须设置endCol属性！");
		// }
		if (endCol != null && endCol.trim().length() != 0) {
			try {
				eflc.setEndCol(Short.parseShort(endCol));
			} catch (NumberFormatException ex1) {
				throw new Exception("id为[" + eflc.getId() + "]的endCol属性必须是短整形(short)！");
			}

			if (eflc.getEndCol() < 0) {
				throw new Exception("id为[" + eflc.getId() + "]的标签的getEndCol必须大于等于0！");
			}
		}

		// 解析endStr，并效验属性合法性
		String endStr = eFL.attributeValue("endStr");
		eflc.setEndstr(endStr);

		// 解析sql属性，并效验属性合法性
		String sqls = eFL.attributeValue("sql");
		if ((eflc.getOrgProcedure() == null || eflc.getOrgProcedure().trim().length() == 0)
				&& (sqls == null || sqls.trim().length() == 0)) {
			// 如果没有设置sql，则是循环容器，也就是块循环。循环容器不做入库操作
			eflc.setLoopContenter(true);
		} else if (eflc.getOrgProcedure() != null && eflc.getOrgProcedure().trim().length() != 0 && sqls != null
				&& sqls.trim().length() != 0) {
			throw new Exception("id为[" + eflc.getId() + "]的子标签不能同时设置sql或者procedure属性！");
		} else {
			// 否则就是普通循环
			if (sqls != null && sqls.trim().length() != 0) {
				eflc.setOrgiSqls(sqls.trim());
			}

			// 解析sql
			parseSql(eflc);
		}

		for (Iterator<Element> it = eFL.elementIterator(); it.hasNext();) {
			// 递归解析子配置
			parseFLConfiguration(null, (Element) it.next(), eflc);
		}
	}

	/**
	 * 解析sql可能包含多个sql，每个sql之间使用;号隔开
	 * 
	 * @param eflc
	 *            ExcelFLConfig
	 */
	private void parseSql(ExcelFLConfig eflc) throws Exception {
		if (eflc.getOrgiSqls() != null && eflc.getOrgiSqls().trim().length() != 0) {
			// 解析sql
			String sqls[] = eflc.getOrgiSqls().split(";");
			for (int i = 0; i < sqls.length; i++) {
				parseSql(eflc, sqls[i]);
			}
		} else {
			// 解析存储过程
			String procedures[] = eflc.getOrgProcedure().split(";");
			for (int i = 0; i < procedures.length; i++) {
				parseSql(eflc, procedures[i]);
			}
		}
	}

	/**
	 * 解析sql
	 * 
	 * @param eflc
	 *            ExcelFLConfig
	 */
	private void parseSql(ExcelFLConfig eflc, String orgSql) throws Exception {
		ExcelSql excelSql = ExcelSqlParser.getInstance(orgSql).parse();

		eflc.addExcelSql(excelSql);
	}

	/**
	 * 解析读取form和loop的公共属性配置
	 * 
	 * @param eFL
	 *            Element
	 * @param parent
	 *            ExcelFLConfig
	 * @param eflc
	 *            ExcelFLConfig
	 * @throws Exception
	 */
	private void readFLCommConf(Element eFL, ExcelFLConfig eflc) throws Exception {
		ExcelFLConfig parent = eflc.getParent();
		// 解析id，并效验属性合法性
		String id = eFL.attributeValue("id");
		if (id == null || id.trim().length() == 0) {
			if (parent == null) {
				throw new Exception("form或者loop的顶层配置中有标签没有设置id！");
			} else {
				throw new Exception("标签id为[" + parent.getId() + "]的子form或者loop配置中有标签没有设置id！");
			}
		}
		eflc.setId(id);

		// 解析startRow，并效验属性合法性
		String startRow = eFL.attributeValue("startRow");
		if ((startRow == null || startRow.trim().length() == 0) && parent == null) {
			throw new Exception("form或者loop的顶层配置中必须设置startRow属性！");
		}

		if (startRow != null && startRow.trim().length() != 0) {
			try {
				eflc.setStartRow(Integer.parseInt(startRow));
			} catch (NumberFormatException ex) {
				throw new Exception("id为[" + eflc.getId() + "]的标签的startRow必须为数字！");
			}

			if (eflc.getStartRow() < 0) {
				throw new Exception("id为[" + eflc.getId() + "]的标签的startRow必须大于0！");
			}
		}

		String justMemory = eFL.attributeValue("justMemory");
		if (justMemory != null && justMemory.trim().equals("true")) {
			eflc.setJustMemory(true);
		}

		String procedure = eFL.attributeValue("procedure");
		if (procedure != null && procedure.trim().length() != 0) {
			eflc.setOrgProcedure(procedure.trim());
		}

	}

	/**
	 * 解析配置
	 * 
	 * @param loopElem
	 *            Element
	 * @throws Exception
	 * @return ExcelDBConfiguration
	 */
	private void parseConfiguration(Element confElem) throws Exception {
		// 获取数据库连接、关闭实现类
		String value = confElem.attributeValue("connectionSupervise");
		if (value != null && value.trim().length() != 0) {
			configManager.setConnectionSuperviseClass(value.trim());
		}

		// 解析sheet配置
		for (Iterator<Element> it = confElem.elementIterator("sheet"); it.hasNext();) {
			Element eSheet = (Element) it.next();
			String sheetName = eSheet.attributeValue("name");
			ExcelSheetConfig esc = new ExcelSheetConfig();
			esc.setSheetName(sheetName);
			// 添加配置对象
			configManager.putExcelSheetConfig(sheetName, esc);

			// 解析location配置
			parseLocation(esc, eSheet);
			// 解析sheet下配置的form或者loop
			for (Iterator<Element> itFL = eSheet.elementIterator(); itFL.hasNext();) {
				parseFLConfiguration(esc, (Element) itFL.next(), null);
			}
		}

	}

	public int getCommitCount() {
		return commitCount;
	}

	public void setCommitCount(int commitCount) {
		this.commitCount = commitCount;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getExceptionCount() {
		return exceptionCount;
	}

	public void setExceptionCount(int exceptionCount) {
		this.exceptionCount = exceptionCount;
	}

}