<%-- 
    Document   : index
    Created on : 27-gen-2018, 11.02.27
    Author     : matteo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="assets/css/ext/nv.d3.min.css" media="all" rel="stylesheet">
        <style>
            #chart svg {
              height: 400px;
            }
        </style>
        <title>SafePunch</title>
        <script type="text/javascript" src="assets/js/ext/jquery-3.3.1.min.js"></script>
        <script type="text/javascript" src="assets/js/ext/d3.min.js"></script>
        <script type="text/javascript" src="assets/js/ext/nv.d3.min.js"></script>
        <script type="text/javascript" src="assets/js/sp/service.js"></script>
        <script type="text/javascript" src="assets/js/sp/controller.js"></script>
        <script type="text/javascript">
            var controller=null;
            jQuery(document).ready(function(){
                controller=new SP_Controller();
                // il controller setup binda il click dei pulsanti
                // e degli input alle chiamate del controller
                // poi verrà sostituito direttamente da angularJs
                // che chiamerà direttamente i metodi del controller
                controller.setup();
            });
        </script>
    </head>
    <body>
        <h1>Safe Punch</h1>
        <div class="sampler">
            <label>Sampler</label>
            <input type="number" name="sampler" value="2">
        </div>
        <div class="gain">
            <label>Gain</label>
            <input type="number" name="gain" value="2">
        </div>
        <div class="fullscale">
            <label>Fullscale</label>
            <input type="number" name="fullscale" value="32767">
        </div>
        <div class="idle">
            <label>Idle (ms)</label>
            <input type="number" name="idle" value="100">
        </div>
        <button class="start-command">Start Measure</button>
        <button class="stop-command">Stop Measure</button>
        <div class="content">
            
        </div>
        <div id="chart">
            <svg></svg>
        </div>
    </body>
</html>
