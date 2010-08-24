<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>


<html>
<head>
	
	<link rel="stylesheet" href="../css/jquery-ui-1.8.4.custom.css" type="text/css" />
	<link rel="stylesheet" href="../css/ui.jqgrid.css" type="text/css" />
	<link rel="stylesheet" href="../css/productGrid.css" type="text/css" />

	<script type="text/javascript" src="../js/json_sans_eval.js"></script>
	<script type="text/javascript" src="../js/jquery-1.4.2.min.js"></script>
	<script type="text/javascript" src="../js/jquery.blockUI.js"></script>
	<script type="text/javascript" src="../js/jquery.jqGrid.min.js"></script>
	<script type="text/javascript" src="../js/qcdGrid.js"></script>
	
	<script type="text/javascript">

		var colNames = new Array();
		var colModel = new Array();
		<c:forEach items="${gridDefinition.columns}" var="column">
			colNames.push("<spring:message code="products.column.${column.name}"/>");
			colModel.push({name:"${column.fields[0].name}", index:"${column.fields[0].name}", width:100, sortable: false});
		</c:forEach>

		var productsGrid;
		
		jQuery(document).ready(function(){

			productsGrid = new QCDGrid({
				element: 'productsGrid',
				dataSource: "list/data.html",
				deleteUrl: "list/delete.html", 
				height: 450, 
				colNames: colNames,
				colModel: colModel,
				multiselect: true,
				loadingText: 'Wczytuje...',
				ondblClickRow: function(id){
			        window.location='addModifyEntityForm.html?entityId='+id
		        }
			});
			productsGrid.refresh();

			$('#sortColumnChooser').val("${gridDefinition.columns[0].fields[0].name}");
			$('#sortOrderChooser').val('asc');
		});

		sort = function() {
			var column = $('#sortColumnChooser').val();
			var order = $('#sortOrderChooser').val();
			console.debug('sort: '+column+' - '+order);
			productsGrid.setSortOptions(column, order);
			productsGrid.refresh();
		}
		
	
	</script>

</head>
<body>

	<div id="pageHeader">${headerContent}</div>
	
	<div id="messageBox">${message}</div>
	
	<div id="topButtons">
		<button onClick="window.location='addModifyEntityForm.html'"><spring:message code="productsGridView.new"/></button>
		<button onClick="productsGrid.refresh()"><spring:message code="productsGridView.refresh"/></button>
		<button onClick="productsGrid.deleteSelectedRecords()"><spring:message code="productsGridView.delete"/></button>
	</div>
	<div id="sortButtons">
		<select id='sortColumnChooser'>
			<c:forEach items="${gridDefinition.columns}" var="column">
				<option value='${column.fields[0].name}'><spring:message code="products.column.${column.name}"/></option>
			</c:forEach>
		</select>
		<select id='sortOrderChooser'>
			<option value='asc'><spring:message code="productsGridView.asc"/></option>
			<option value='desc'><spring:message code="productsGridView.desc"/></option>
		</select>
		<button onClick="sort()"><spring:message code="productsGridView.sort"/></button>
	</div>
	
	<table id="productsGrid"></table> 
	
</body>
</html>