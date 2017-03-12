!function( $ ) {
	var Datagrid = function (element, options) {
		this.$element = $(element);
		var _tth_h = "<thead></thead>";
		this.$element.append(_tth_h);
		var _tfoot_h = "<tfoot><tr><th>";
		_tfoot_h += "<div class='datagrid-footer-left' style='display:none;'>" +
				"<div class='grid-controls'><span><span class='grid-start'></span> - <span class='grid-end'></span> of <span class='grid-count'></span></span>";
		var _pageSelectList = options.dataSource.pageSelect();
		if(_pageSelectList){
			_tfoot_h += "<select class='grid-pagesize'>";
			$.each(_pageSelectList, function(i, value){
				_tfoot_h += "<option>" + value + "</option>";
			});
			_tfoot_h += "</select>";
		}
		else{
			_tfoot_h += "<select class='grid-pagesize'><option>5</option><option>10</option><option>20</option></select>";
		}
		_tfoot_h += "<span>Per Page</span></div></div>";
		
		_tfoot_h += "<div class='datagrid-footer-right' style='display:none;'>" +
				"<div class='grid-pager'><button class='btn btn-default btn-sm grid-prevpage'><i class='glyphicon glyphicon-chevron-left'></i></button><span>Page</span>" +
				"<input type='text'><span>of <span class='grid-pages'></span></span>" +
				"<button class='btn btn-default btn-sm grid-nextpage'><i class='glyphicon glyphicon-chevron-right'></i></button></div></div></th></tr></tfoot>";
		
		this.$element.append(_tfoot_h);
		
		this.$thead = this.$element.find('thead');
		this.$tfoot = this.$element.find('tfoot');
		this.$footer = this.$element.find('tfoot th');
		this.$footerchildren = this.$footer.children().show().css('visibility', 'hidden');
		this.$searchcontrol = this.$element.find('.search');
		this.$pagesize = this.$element.find('.grid-pagesize');
		this.$pageinput = this.$element.find('.grid-pager input');
		this.$pagedropdown = this.$element.find('.grid-pager .dropdown-menu');
		this.$prevpagebtn = this.$element.find('.grid-prevpage');
		this.$nextpagebtn = this.$element.find('.grid-nextpage');
		this.$pageslabel = this.$element.find('.grid-pages');
		this.$countlabel = this.$element.find('.grid-count');
		this.$startlabel = this.$element.find('.grid-start');
		this.$endlabel = this.$element.find('.grid-end');

		this.$tbody = $('<tbody>').insertAfter(this.$thead);
		this.$colheader = $('<tr>').appendTo(this.$thead);

		this.options = $.extend(true, {}, $.fn.datagrid.defaults, options);
		this.options.dataOptions.pageSize = parseInt(this.$pagesize.val(), 10);
		this.columns = this.options.dataSource.columns();

		this.$nextpagebtn.on('click', $.proxy(this.next, this));
		this.$prevpagebtn.on('click', $.proxy(this.previous, this));
		this.$searchcontrol.on('searched cleared', $.proxy(this.searchChanged, this));
		this.$colheader.on('click', 'th', $.proxy(this.headerClicked, this));
		this.$pagesize.on('change', $.proxy(this.pagesizeChanged, this));
		this.$pageinput.on('change', $.proxy(this.pageChanged, this));
		this.$pagedropdown.on('click', $.proxy(this.pagedropdownChanged, this));

		this.renderColumns();

		if (this.options.stretchHeight) this.initStretchHeight();

		this.renderData();
	};

	Datagrid.prototype = {

		constructor: Datagrid,

		renderColumns: function () {
			var self = this;

			this.$footer.attr('colspan', this.columns.length);

			var colHTML = '';

			$.each(this.columns, function (index, column) {
				colHTML += '<th data-property="' + column.property + '"';
				if (column.sortable) colHTML += ' class="sortable"';
				colHTML += 'style="text-align:center">' + column.label + '</th>';
			});

			self.$colheader.append(colHTML);
		},

		updateColumns: function ($target, direction) {
			var className = (direction === 'asc') ? 'icon-chevron-up' : 'icon-chevron-down';
			this.$colheader.find('i').remove();
			this.$colheader.find('th').removeClass('sorted');
			$('<i>').addClass(className).appendTo($target);
			$target.addClass('sorted');
		},

		updatePageDropdown: function (data) {
			var pageHTML = '';

			for (var i = 1; i <= data.pages; i++) {
				pageHTML += '<li><a>' + i + '</a></li>';
			}

			this.$pagedropdown.html(pageHTML);
		},

		updatePageButtons: function (data) {
			if (data.page === 1) {
				this.$prevpagebtn.attr('disabled', 'disabled');
			} else {
				this.$prevpagebtn.removeAttr('disabled');
			}

			if (data.page === data.pages) {
				this.$nextpagebtn.attr('disabled', 'disabled');
			} else {
				this.$nextpagebtn.removeAttr('disabled');
			}
		},

		renderData: function () {
			var self = this;

			this.options.dataSource.data(this.options.dataOptions, function (data) {
				var itemdesc = (data.count === 1) ? self.options.itemText : self.options.itemsText;
				var rowHTML = '';

				self.$footerchildren.css('visibility', function () {
					return (data.count > 0) ? 'visible' : 'hidden';
				});

				self.$pageinput.val(data.page);
				self.$pageslabel.text(data.pages);
				self.$countlabel.text(data.count + ' ' + itemdesc);
				self.$startlabel.text(data.start);
				self.$endlabel.text(data.end);

				self.updatePageDropdown(data);
				self.updatePageButtons(data);

				$.each(data.data, function (index, row) {
					rowHTML += '<tr>';
					$.each(self.columns, function (index, column) {
						var tdValue = row[column.property];
						if(column.render){
							tdValue = column.render(tdValue, row, index);
						}
						if(tdValue != null){
							if(column.align){
								rowHTML += '<td style="text-align:' + column.align + '">' + tdValue + '</td>';
							}
							else{
								rowHTML += '<td>' + tdValue + '</td>';
							}
						}
						else{
							rowHTML += '<td></td>';
						}
					});
					rowHTML += '</tr>';
				});

				if (!rowHTML) rowHTML = self.placeholderRowHTML('0 ' + self.options.itemsText);

				self.$tbody.html(rowHTML);
				self.stretchHeight();

				self.$element.trigger('loaded');
			});

		},

		placeholderRowHTML: function (content) {
			return '<tr><td style="text-align:center;padding:20px;border-bottom:none;" colspan="' +
				this.columns.length + '">' + content + '</td></tr>';
		},

		headerClicked: function (e) {
			var $target = $(e.target);
			if (!$target.hasClass('sortable')) return;

			var direction = this.options.dataOptions.sortDirection;
			var sort = this.options.dataOptions.sortProperty;
			var property = $target.data('property');

			if (sort === property) {
				this.options.dataOptions.sortDirection = (direction === 'asc') ? 'desc' : 'asc';
			} else {
				this.options.dataOptions.sortDirection = 'asc';
				this.options.dataOptions.sortProperty = property;
			}

			this.options.dataOptions.pageIndex = 0;
			this.updateColumns($target, this.options.dataOptions.sortDirection);
			this.renderData();
		},

		pagesizeChanged: function (e) {
			this.options.dataOptions.pageSize = parseInt($(e.target).val(), 10);
			this.options.dataOptions.pageIndex = 0;
			this.renderData();
		},

		pageChanged: function (e) {
			this.options.dataOptions.pageIndex = parseInt($(e.target).val(), 10) - 1;
			this.renderData();
		},

		searchChanged: function (e, search) {
			this.options.dataOptions.search = search;
			this.options.dataOptions.pageIndex = 0;
			this.renderData();
		},
		
		pagedropdownChanged: function (e) {
			this.$pageinput.val($(e.target).html());
			this.options.dataOptions.pageIndex = parseInt(this.$pageinput.val(), 10) - 1;
			this.renderData();
		},

		previous: function () {
			this.options.dataOptions.pageIndex--;
			this.renderData();
		},

		next: function () {
			this.options.dataOptions.pageIndex++;
			this.renderData();
		},

		reload: function (param) {
			this.options.dataOptions.pageIndex = 0;
			var _param = "";
			if(param){
				var i = 0;
				$.each(param, function(name, value) {
					if(i == 0){
						_param += name + "=" + value;
					}
					else{
						_param += "&" + name + "=" + value;
					}
					i++;
				});
			}
			this.options.dataOptions.param = _param;
			this.renderData();
		},

		initStretchHeight: function () {
			this.$gridContainer = this.$element.parent();

			this.$element.wrap('<div class="datagrid-stretch-wrapper">');
			this.$stretchWrapper = this.$element.parent();

			this.$headerTable = $('<table>').attr('class', this.$element.attr('class'));
			this.$footerTable = this.$headerTable.clone();

			this.$headerTable.prependTo(this.$gridContainer).addClass('datagrid-stretch-header');
			this.$thead.detach().appendTo(this.$headerTable);

			this.$sizingHeader = this.$thead.clone();
			this.$sizingHeader.find('tr:first').remove();

			this.$footerTable.appendTo(this.$gridContainer).addClass('datagrid-stretch-footer');
			this.$tfoot.detach().appendTo(this.$footerTable);
		},

		stretchHeight: function () {
			if (!this.$gridContainer) return;

			this.setColumnWidths();

			var targetHeight = this.$gridContainer.height();
			var headerHeight = this.$headerTable.outerHeight();
			var footerHeight = this.$footerTable.outerHeight();
			var overhead = headerHeight + footerHeight;

			this.$stretchWrapper.height(targetHeight - overhead);
		},

		setColumnWidths: function () {
			if (!this.$sizingHeader) return;

			this.$element.prepend(this.$sizingHeader);

			var $sizingCells = this.$sizingHeader.find('th');
			var columnCount = $sizingCells.length;

			function matchSizingCellWidth(i, el) {
				if (i === columnCount - 1) return;
				$(el).width($sizingCells.eq(i).width());
			}

			this.$colheader.find('th').each(matchSizingCellWidth);
			this.$tbody.find('tr:first > td').each(matchSizingCellWidth);

			this.$sizingHeader.detach();
		}
	};


	// DATAGRID PLUGIN DEFINITION

	$.fn.datagrid = function (option, param) {
		return this.each(function () {
			var $this = $(this);
			var data = $this.data('datagrid');
			var options = typeof option === 'object' && option;

			if (!data) $this.data('datagrid', (data = new Datagrid(this, options)));
			if (typeof option === 'string') data[option](param);
		});
	};

	$.fn.datagrid.defaults = {
		dataOptions: { pageIndex: 0, pageSize: 5, param: "" },
		loadingHTML: '<div class="progress"><div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 60%;"><span class="sr-only">60% Complete</span></div></div>',
		itemsText: 'items',
		itemText: 'item'
	};

	$.fn.datagrid.Constructor = Datagrid;

}( window.jQuery )


var girdDataSource = function (options) {
		this._formatter = options.formatter;
		this._columns = options.columns;
		this._delay = options.delay || 250;
		this._data = options.data;
		this._pageSelect = options.pageSelect;
		this._loadFromServer = (options.loadFromServer == false) ? options.loadFromServer : true;
	};
	
girdDataSource.prototype = {

	columns : function() {
		return this._columns;
	},
	
	pageSelect : function() {
		return this._pageSelect;
	},

	data : function(options, callback) {
		var self = this;
		
		setTimeout(function() {
			
			if(self._loadFromServer){
				var data = null;
				var param = options.param;
				var page = options.pageIndex + 1;
				var count = null;
				
				if(param.indexOf("=") != -1){
					param = param + "&pageNo=" + page + "&limit=" + options.pageSize;
				}
				else{
					param = "pageNo=" + page + "&limit=" + options.pageSize;
				}
				if(options.sortProperty){
					param = param + "&sortName=" + options.sortProperty + "&sortOrder=" + options.sortDirection;
				}
				param = param + "&t=" + new Date();
				var ajaxOptions = {
					type: "POST",
					url: self._data,
					data: param,
					async: true,
					dataType: 'json',
					success: function (serverData){
						if(serverData.total != "undefined"){
							data = serverData.rows;
							count = serverData.total;
							var startIndex = options.pageIndex * options.pageSize;
							var endIndex = startIndex + options.pageSize;
							var end = (endIndex > count) ? count : endIndex;
							var pages = Math.ceil(count / options.pageSize);
							var start = startIndex + 1;
							if (self._formatter)
								self._formatter(data);
							callback({
								data : data,
								start : start,
								end : end,
								count : count,
								pages : pages,
								page : page
							});
						}else{
							bootbox.alert("加载列表失败,请联系管理员!");
						}
						
					},
					error: function (XMLHttpRequest, textStatus, errorThrown){
						bootbox.alert("加载列表失败,请联系管理员!");
					}
				};
				$.ajax(ajaxOptions);
			}
			else{
				var data = $.extend(true, [], self._data);
				
				var count = data.length;

				if (options.sortProperty) {
					if (options.sortDirection === 'desc')
						data.reverse();
				}

				var startIndex = options.pageIndex * options.pageSize;
				var endIndex = startIndex + options.pageSize;
				var end = (endIndex > count) ? count : endIndex;
				var pages = Math.ceil(count / options.pageSize);
				var page = options.pageIndex + 1;
				var start = startIndex + 1;

				data = data.slice(startIndex, endIndex);
				if (self._formatter)
					self._formatter(data);

				callback({
					data : data,
					start : start,
					end : end,
					count : count,
					pages : pages,
					page : page
				});
			}
			
		}, this._delay)
	}
};
