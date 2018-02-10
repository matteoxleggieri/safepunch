/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function SP_Service() {    
    this.serverUrl='service';
    this.popDataHandler=null;
    this.popDataIdle=100;
    this.started=false;
}

SP_Service.prototype.doAjaxRequest=function(request, callback, callbackArgs){
    var self=this;
    jQuery.ajax({
        url: self.serverUrl,
        type: 'POST',
        dataType: 'json',
        data: request,
        complete: function(response){
            if(callback){
                callback(response.responseJSON, callbackArgs);
            }
        }
    });
};

SP_Service.prototype.init=function(){
    var self=this;
};

SP_Service.prototype.startPopData=function(callback, callbackArgs){
    var self=this;
    self.started=true;
    self.popDataHandler=setInterval(function(){
        var request={
            command: 'popData'
        };
        self.doAjaxRequest(request, callback, callbackArgs);
    }, self.popDataIdle);    
};

SP_Service.prototype.stopPopData=function(){
    var self=this;
    self.started=false;
};

SP_Service.prototype.onEmptyData=function(){
    var self=this;
    if(!self.started && self.popDataHandler!==null){
        clearInterval(self.popDataHandler);
    }
};



