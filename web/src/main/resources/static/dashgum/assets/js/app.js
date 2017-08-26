const APP_URL = 'http://localhost:8080/graph/';
const APPLICATION_JSON = 'application/json; charset=UTF-8';


$(document).ready(function() {
    $("#lookup").on("click", function() {
        var graphName = $("#graphName").val();
        refreshTree(graphName);
    });
});

function refreshTree(graphName) {
    var tree = $('#tree').tree({
      primaryKey: 'id',
      dragAndDrop: true,
      dataSource: APP_URL + 'render/' + graphName
      //dataSource: [ { text: 'foo', myChildrenNode: [ { text: 'bar' } ] } ]
    }
  );

tree.on('select', function (e, node, id) {
    alert('select is fired.');
});

  tree.on('nodeDrop', function (e, id, parentId, orderNumber) {
      var params = { id: id, parentId: parentId, orderNumber: orderNumber };
      alert(JSON.stringify(params));
    }
  );
}
