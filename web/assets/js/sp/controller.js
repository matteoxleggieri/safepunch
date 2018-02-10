/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function SP_Controller() {    
    this.service=new SP_Service();
    this.service.init();
    this.chart=null;
    this.datum=null;
    this.x=0;
    this.chartData=[
        {
            values: [],
            key: 'Values',
            color: '#ff7f0e'
        }
    ];
}

SP_Controller.prototype.setup=function(){
    var self=this;
    jQuery('.start-command').click(function(){
        var sampler=jQuery('.sampler input').val();
        var gain=jQuery('.gain input').val();
        var fullscale=jQuery('.fullscale input').val();
        var idle=jQuery('.idle input').val();
        var data={
            command: 'start',
            sampler: sampler,
            gain: gain,
            fullscale: fullscale,
            idle: idle
        };
        self.service.doAjaxRequest(data, function(data){
            if(data && data.text && data.text==='OK'){
                self.service.startPopData(self.onPopData, [self, self.service]);
            }
        });
    });
    jQuery('.stop-command').click(function(){        
        var data={
            command: 'stop'
        };
        self.service.doAjaxRequest(data, function(data){
            if(data && data.text && data.text==='OK'){
                self.service.stopPopData();
            }
        });
    });
    self.setupChart(self.chartData);
};

SP_Controller.prototype.onPopData=function(data, args){
    var controller=args[0];
    var service=args[1];
    if(data && data.value){
        //var $content=jQuery('.content');
        //$content.text(data.text);
        if(controller.x>100){
            controller.chartData[0].values.splice(0, 1);
        }
        controller.chartData[0].values.push({x: controller.x, y: data.value});
        controller.datum.datum(controller.chartData).transition().duration(500).call(controller.chart);
        nv.utils.windowResize(controller.chart.update);
        controller.x++;
    } else {
        service.onEmptyData();
    }    
};

SP_Controller.prototype.setupChart=function(data) {
    var self=this;    
    nv.addGraph(function() {
        self.chart = nv.models.lineChart()
          .useInteractiveGuideline(true)
          ;

        self.chart.xAxis
          .axisLabel('Time (ms)')
          .tickFormat(d3.format(',r'))
          ;

        self.chart.yAxis
          .axisLabel('Voltage (v)')
          .tickFormat(d3.format('.02f'))
          ;

        
        self.datum = d3.select('#chart svg')
          .datum(data)
          ;
        self.datum.transition().duration(500)
          .call(self.chart)
          ;

        nv.utils.windowResize(self.chart.update);

        return self.chart;
    });
};


