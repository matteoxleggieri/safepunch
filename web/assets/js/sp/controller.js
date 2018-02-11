/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function SP_Controller() {    
    this.service=new SP_Service();
    this.service.init();
    this.chart=null;
    this.chartDatum=null;
    this.xAxisOffset=0;
    this.xAxisSpan=10000;
    this.chartSeries=[
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
        var idleServer=jQuery('.idle-server input').val();
        var idleDevice=jQuery('.idle-device input').val();
        var data={
            command: 'start',
            sampler: sampler,
            gain: gain,
            fullscale: fullscale,
            idleServer: idleServer,
            idleDevice: idleDevice
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
    self.setupChart();
};

SP_Controller.prototype.onPopData=function(data, args){
    var controller=args[0];
    var service=args[1];
    if(data && data.value && data.timestamp){
        var firstSeries=controller.chartSeries[0];
        if(controller.xAxisOffset===0){
            controller.xAxisOffset=data.timestamp;
        } else if((data.timestamp-controller.xAxisOffset)>controller.xAxisSpan) {
            firstSeries.values.splice(0, 1);
        }
        var date=new Date(data.timestamp);
        firstSeries.values.push({x: date, y: data.value});
        controller.chartDatum.datum(controller.chartSeries).transition().duration(500).call(controller.chart);
        nv.utils.windowResize(controller.chart.update);
    } else {
        service.onEmptyData();
    }    
};

SP_Controller.prototype.setupChart=function() {
    var self=this;    
    nv.addGraph(function() {
        self.chart = nv.models.lineChart()
          .useInteractiveGuideline(true)
          ;

        self.chart.xAxis
          .axisLabel('Time (HH:MM:SS.MS)')
          .tickFormat(function(d){
              return d3.time.format('%H:%M:%S.%L')(new Date(d));
          });

        self.chart.yAxis
          .axisLabel('Voltage (v)')
          .tickFormat(d3.format('.05f'))
          ;

        
        self.chartDatum = d3.select('#chart svg')
          .datum(self.chartSeries)
          ;
        self.chartDatum.transition().duration(500)
          .call(self.chart)
          ;

        nv.utils.windowResize(self.chart.update);

        return self.chart;
    });
};


