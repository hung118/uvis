DwrYuiDataSource = function(remoteMethod) {
    this.remoteMethod = remoteMethod;
    this._init();
};

DwrYuiDataSource.prototype = new YAHOO.widget.DataSource();


DwrYuiDataSource.prototype.doQuery = function(oCallbackFn,
sQuery, oParent) {
    var oSelf = this;
    this.remoteMethod(sQuery, function(aResults) {
        var resultObj = {};
        resultObj.query = decodeURIComponent(sQuery);
        resultObj.results = aResults;
        oSelf._addCacheElem(resultObj);      
        oSelf.getResultsEvent.fire(oSelf, oParent, sQuery, aResults);
        oCallbackFn(sQuery, aResults, oParent);
    });
}; 