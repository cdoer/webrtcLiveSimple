/**
 * Created by yang on 2018-08-03.
 */
webos.register("index",function(params){
    $("#createRoom").click(function(){
        layer.prompt({title: '输入会议名称和会议密码(可不填)，格式：会议名称=会议密码', formType: 0}, function(pass, index){
            var infos = pass.split("=");
            var params = {};
            if(infos.length==1){
                params.title = infos[0];
                if(!params.title){
                    layer.msg("请输入会议名称");
                    return;
                }
            }else{
                params.title = pass.split("=")[0];
                params.pwd = pass.split("=")[1];
            }
            webos.request({
               url:"ActionServlet?method=createRoom",
                data:params,
                success:function(data){
                    if(data.success){
                        layer.close(index);
                        //跳转到会议室
                        window.open("/chatRoom.jsp");
                        refresh();
                    }else{
                        layer.msg(data.data);
                    }
                }
            });
        })
    });


    $("#joinRoom2").click(function(){
        var roomName = $(this).attr("roomName");
        var hasPwd = $(this).attr("hasPwd");
        if(hasPwd=="true"){
            layer.prompt({title: '请输入该会议室的密码', formType: 0}, function(pass, index){
                webos.request({
                    url:"ActionServlet?method=joinRoom",
                    data:{
                        roomName:roomName,
                        pwd:pass
                    },
                    success:function(data){
                        if(data.success){
                            layer.close(index);
                            //跳转到会议室
                            window.open("/chatRoom.jsp");
                        }else{
                            layer.msg(data.data);
                        }
                    }
                })
            })
        }else{
            webos.request({
                url:"ActionServlet?method=joinRoom",
                data:{
                    roomName:roomName
                },
                success:function(data){
                    if(data.success){
                        //跳转到会议室
                        window.open("/chatRoom.jsp");
                        refresh();
                    }else{
                        layer.msg(data.data);
                    }
                }
            })
        }
    });

    function refresh(){
        location.href =location.href;
    }

    $("#joinRoom").click(function(){
        layer.prompt({title: '请输入该会议ID密码(密码选填)，格式:会议ID=密码', formType: 0}, function(pass, index){
            if(!pass){
                layer.msg("请输入会议ID和密码(密码选填)");
                return;
            }
            var infos = pass.split("=");
            webos.request({
                url:"ActionServlet?method=joinRoom",
                data:{
                    roomName:infos[0],
                    pwd:infos.length>1?infos[1]:null
                },
                success:function(data){
                    if(data.success){
                        //跳转到会议室
                        window.open("/chatRoom.jsp");
                        layer.close(index);
                        refresh();
                    }else{
                        layer.msg(data.data);
                    }
                }
            })
        })
    });


    $("#exitRoom").click(function(){
        layer.confirm('确定退出会议？', function(index, layero){
            webos.request({
                url:"ActionServlet?method=exitRoom",
                success:function(data){
                    if(data.success){
                        //跳转到会议室
                        location.href = location.href;
                    }else{
                        layer.msg(data.data);
                    }
                }
            })
            layer.close(index);
        }, function(index){
            layer.close(index);
        });
    });


    $("#endChat").click(function(){
        layer.confirm('确定解散会议？', function(index, layero){
            webos.request({
                url:"ActionServlet?method=endChat",
                success:function(data){
                    if(data.success){
                        location.href = location.href;
                    }else{
                        layer.msg(data.data);
                    }
                }
            })
            layer.close(index);
        }, function(index){
            layer.close(index);
        });
    });



});


