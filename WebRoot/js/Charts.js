Ext.require('Ext.chart.*');
Ext.require(['Ext.layout.container.Fit', 'Ext.window.MessageBox']);

Ext.onReady(function() {

    var downloadChart = function(chart){
        Ext.MessageBox.confirm('Confirm Download', 'Would you like to download the chart as an image?', function(choice){
            if(choice == 'yes'){
                chart.save({
                    type: 'image/png'
                });
            }
        });
    };

    var chart1 = Ext.create('Ext.chart.Chart',{
            animate: false,
            store: datastore,
            insetPadding: 30,
            axes: [{
                type: 'Numeric',
                minimum: 0,
                position: 'left',
                fields: ['rate'],
                title: false,
                grid: true,
                label: {
                   
                    font: '10px Arial'
                }
            }, {
                type: 'Category',
                position: 'bottom',
                fields: ['hour'],
                title: false,
                label: {
                    font: '11px Arial'
                    
                }
            }],
            series: [{
                type: 'line',
                axis: 'left',
                xField: 'hour',
                yField: 'rate',
                listeners: {
                  itemmouseup: function(item) {
                      Ext.example.msg('Item Selected', item.value[1] + ' visits on ' + Ext.Date.monthNames[item.value[0]]);
                  }  
                },
                tips: {
                    trackMouse: true,
                    width: 80,
                    height: 40,
                    renderer: function(storeItem, item) {
                        this.setTitle('time : ' + storeItem.get('hour'));
                        this.update(storeItem.get('rate'));
                    }
                },
                style: {
                    fill: '#38B8BF',
                    stroke: '#38B8BF',
                    'stroke-width': 3
                },
                markerConfig: {
                    type: 'circle',
                    size: 4,
                    radius: 4,
                    'stroke-width': 0,
                    fill: '#38B8BF',
                    stroke: '#38B8BF'
                }
            }]
        });
    
    var combostore = Ext.create('Ext.data.JsonStore', {
    	
    	fields : ['name','value'],
    	proxy : {
    		type : 'ajax',
    		url : 'combo'
    	}
    
    	
    });
   //combostore.load();
    
    var combp =  Ext.create('Ext.form.ComboBox', {
    	store : combostore,
    	displayField: 'name',
        valueField: 'value',
        listeners: {
        	'select' : function(){
        		
        		 Ext.Ajax.request({
        		    	url : 'service',
        		    	params : {
        		    		service : combp.getValue() 
        		    	},
        		    	success : function(response){
        		    		var text =  response.responseText;
        		    		
        		    		window.datastore =  Ext.create('Ext.data.JsonStore',{
        		    	    	fields: ['hour', 'rate'],
        		    	    	data :Ext.JSON.decode(text)
        		    	    });
        		    		
        		    		chart1.store =  window.datastore;
        		    		chart1.redraw();
        		    	    
        		    	}
        		    });
        	}
        	
        }
       
    });
    
    var panel1 = Ext.create('widget.panel', {
        width: 1020,
        height: 800,
        title: 'Service Prediction',
        renderTo: Ext.getBody(),
        layout: 'border',
        tbar: [{
            text: 'Save Chart',
            handler: function(){ downloadChart(chart1); }
        },combp ],
        items:  chart1 
    });
    
    
    Ext.Ajax.request({
    	url : 'service',
    	success : function(response){
    		var text =  response.responseText;
    		
    		window.datastore =  Ext.create('Ext.data.JsonStore',{
    	    	fields: ['hour', 'rate'],
    	    	data : Ext.JSON.decode(text)
    	    });
    		
    		chart1.store =  window.datastore;
    		chart1.redraw();
    	    
    	}
    });
  
});
