<!doctype html>
<html>

<head design-width="750">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="format-detection" content="telephone=no">
    <title></title>
    <link rel="stylesheet" href="css/reset.css">
    <!--重置样式-->
    <link rel="stylesheet" href="css/style.css?v=202012">
    <!--页面样式-->
    <link rel="stylesheet" href="css/swiper.min.css">
    <script src="js/auto-size.js"></script>
    <!--设置字体大小-->
    <script src="js/jquery.datetimepicker.fulls.js"></script>
    <script src="js/jquery-1.10.2.min.js"></script>
    <script type="text/javascript" src="js/jquery.qrcode.min.js"></script>

    <script src="https://cdn.bootcdn.net/ajax/libs/vue/2.6.14/vue.js"></script>
    <style>
        .notDown {
            background: #ccc !important;
        }
    </style>

</head>

<body ontouchstart="" onmouseover="" style="background:#00349a;">
<div id="w" style="width:100%; height:100%; position:fixed; z-index:-1; background:url(img/bj.jpg) no-repeat center top;">
</div>
<div class="mobile-wrap center" id="app" style="position:absolute; top:80px; left:0; right:0; margin:0 auto; max-width:750px; background:#FFFFFF; border-radius:30px 30px 0 0; width:96%; overflow:hidden;">
    <main>
        <div class="appItem" v-if="languages">
            <!-- logo -->
            <div class="left"><img :src="downloadData.icon" id="icon" alt=""></div>
            <div class="right">
                <!-- 软件名 -->
                <div style="display: flex;">
                    <strong id="name">{{downloadData.appName}}</strong>
                    <div align="right">
                        <a @click="tishi(1)" href="javascript:;"><img style="width:17px; height:17px; vertical-align:middle;" src="img/renzhen.png">&nbsp;{{languages.safety}}</a>
                    </div>
                </div>
                <div class="installBox">
                    <a :class="['down', notdownLoad ? '' : 'notDown']" @click="onDownload" href="javascript:;">{{languages.download_name}}</a>
                </div>
            </div>

            <!-- <div align="center" style="width:100%; margin-top:20px;">
                <div id="pc" style="display:none;">
                    <div id="output"></div>
                    <p style="line-height:60px; color:#333333">请使用浏览器或相机扫码下载</p>

                </div>
            </div> -->

            <div class="appTip">
                <div class="score">
                    <div class="star">4.9 ★★★★★<var></var></div>
                    <p>19k {{languages.score}}</p>
                </div>
                <div class="centerBox">
                    <b>42w+</b>
                    <p>{{languages.install}}</p>
                </div>
                <div class="age">
                    <b>4+</b>
                    <p>{{languages.age}}</p>
                </div>
            </div>
        </div>

        <div class="comment" v-if="languages">
            <strong class="publicTitle">{{languages.comment}}</strong>
            <div class="left">
                <b>5.0</b>
                <p>{{languages.score_1}} 5 {{languages.score_2}}</p>
            </div>
            <div class="right">
                <div class="star_row">
                    <span class="s1"><i></i></span>
                    <div class="lineBox"><var class="v1"></var></div>
                </div>
                <div class="star_row">
                    <span class="s2"><i></i></span>
                    <div class="lineBox"><var class="v2"></var></div>
                </div>
                <div class="star_row">
                    <span class="s3"><i></i></span>
                    <div class="lineBox"><var class="v3"></var></div>
                </div>
                <div class="star_row">
                    <span class="s4"><i></i></span>
                    <div class="lineBox"><var class="v4"></var></div>
                </div>
                <div class="star_row">
                    <span class="s5"><i></i></span>
                    <div class="lineBox"><var class="v5"></var></div>
                </div>
                <p>19k {{languages.score}}</p>
            </div>
        </div>

        <!--<div class="appInfo">
        <div class="box">
            <ul>

                <li>
                   <span>顺其**然</span><br>
                    <p style="text-align:left">非常好的平台。</p>
                </li>
                <li>
                   <span>A蓝红*</span><br>
                    <p style="text-align:left">我是朋友推荐的，一直坚持打卡！</p>
                </li>
                <li>
                   <span>小镇姑*</span><br>
                    <p style="text-align:left">这个软件真不错，我用了2年了，强烈推荐</p>
                </li>
                <li>
                   <span>陈静*</span><br>
                    <p style="text-align:left">用起来真不多，各方面都好用</p>
                </li>
                <li>
                   <span>温柔的台*</span><br>
                    <p style="text-align:left">朋友推荐下载的</p>
                </li>


                 <div align="center" style="margin:8px 0;"><a style="color:#999999" class="down" href="javascript:;">更多评论</a></div>

            </ul>
        </div>
        </div>-->

        <div class="appInfo" v-if="languages">
            <strong class="publicTitle">{{languages.information}}</strong>
            <div class="box">
                <ul>
                    <li>
                        <span>{{languages.size}}</span>
                        <p>7.5 MB</p>
                    </li>
                    <li>
                        <span>{{languages.language}}</span>
                        <p id="language">{{languages.languageMsg}}</p>
                    </li>
                    <li>
                        <span>{{languages.size_part}}</span>
                        <p>{{languages.size_part_text}}</p>
                    </li>
                    <li>
                        <span>Copyright</span>
                        <p>© 2020 MobiFun Games Inc</p>
                    </li>
                </ul>
            </div>
        </div>
    </main>
    <div class="footer" v-if="languages">
        <p>{{languages.copyright_title}}：</p>
        <p class="p2" style="font-size:10px">{{languages.copyright_text}}</p>
    </div>


    <div id="kai" v-if="kaishow" style="position:absolute; top:10px; left:5%; width:90%; background:#FFFFFF;">
        <div align="right" @click="tishi(0)" style="width:100%; height:20px; color:#333333; font-size:14px; font-weight:600;">{{languages.close}}
        </div>
        <div class="pic">
            <img style="width:100%; height:auto;" v-if="downloadData.language == 'zh'" src="img/0df0c_0_600_411.jpg" alt="">
            <img style="width:100%; height:auto;" v-if="downloadData.language == 'en'" src="img/en-1.png" alt="">
            <img style="width:100%; height:auto;" v-if="downloadData.language == 'zh-tw'" src="img/zhtw-1.png" alt="">
        </div>
        <div style="padding:10px; font-size:14px; font-weight:600;">{{languages.tips_1}}</div>

        <div class="pic">
            <img style="width:100%; height:auto;" v-if="downloadData.language == 'zh'" src="img/0df0c_0_600_411.jpg" alt="">
            <img style="width:100%; height:auto;" v-if="downloadData.language == 'en'" src="img/en-1.png" alt="">
            <img style="width:100%; height:auto;" v-if="downloadData.language == 'zh-tw'" src="img/zhtw-1.png" alt="">
        </div>
        <div style="padding:10px; font-size:14px; font-weight:600;">{{languages.tips_2}}</div>

        <div class="pic">
            <img style="width:100%; height:auto;" v-if="downloadData.language == 'zh'" src="img/9179e_3_600_411.jpg" alt="">
            <img style="width:100%; height:auto;" v-if="downloadData.language == 'en'" src="img/en-2.png" alt="">
            <img style="width:100%; height:auto;" v-if="downloadData.language == 'zh-tw'" src="img/zhtw-2.png" alt="">
        </div>
        <div style="padding:10px; font-size:14px; font-weight:600;">{{languages.tips_3}}</div>

        <div style="padding:20px 10px; font-size:14px; font-weight:600;">{{languages.tips_4}}</div>
    </div>


    <!-- 下载码弹窗 -->
    <div class="downCode-pop" v-if="isDownInput">
        <div class="downCode">
            <div class="input">
                <input v-model="code" placeholder="请输入下载码" type="text">
            </div>
            <div class="btns">
                <button @click="vefiyCode">验证安装</button>
                <button @click="pay">购买下载码</button>
            </div>
        </div>

    </div>




    <div class="pupPic"><img src="img/5cbc4_5_1242_2007.png" alt=""></div>
    <div style="width:100%; height:60px; clear:both"></div>
</div>
<!--mobile_wrap-->

<div id="fz" align="center" style="position:fixed; bottom:0; height:60px; border-top:#CCCCCC 1px solid; background:#FFFFFF; width:100%; display:none;">
    <div>
        <script type="text/javascript">
            function copyUrl2() {
                var Url2 = window.location.href;
                var oInput = document.createElement('input');
                oInput.value = Url2;
                document.body.appendChild(oInput);
                oInput.select(); // 选择对象
                document.execCommand("Copy"); // 执行浏览器复制命令
                oInput.className = 'oInput';
                oInput.style.display = 'none';
                alert('复制成功');
            }
        </script>
        <input style=" height:40px; background:#3399FF; border-radius:20px; margin:10px; text-align:center; padding:4px 20px; color:#ffffff;" type="button" onclick="copyUrl2()" value="点击复制到浏览器打开" />
    </div>
</div>

<script src="js/swiper-4.2.0.min.js"></script>
<script src="./language/index.js"></script>
<!--轮播库-->
<script>
    $("body").css("cursor", "pointer");



    var ua = navigator.userAgent.toLowerCase();
    var Sys = {};
    var language = languageConfig
    let t;
    var s;
    (s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1]: 0;

    // 测试
    // const baseUrl = 'http://183.162.157.229:8081'
    const baseUrl = location.origin


    new Vue({
        el: "#app",
        data() {
            return {
                languages: null,
                downloadData: null,
                kaishow: false,
                isDownInput: false,
                base64Data: null,
                code: '',
                notdownLoad: true
            }
        },
        methods: {
            onDownload() {

                if(!that.notdownLoad){
                    location.href = this.downloadData.ipa
                    setTimeout(() => {
                        location.href = this.pro
                    }, 3000)
                }

            },
            tishi(s) {
                console.log(s)
                this.kaishow = s > 0 ? true : false
            },
            handleBase64(str) {
                return decodeURIComponent(atob(str).split('').map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)).join(''))
            },
            getdownCode() {
                let info = this.getRequest('info')
                if (!info) return false

                this.base64Data = JSON.parse(this.handleBase64(info))

                this.createShowData(this.base64Data)

                if (this.base64Data.downCode == 1) {
                    this.isDownInput = true
                } else {
                    this.hasDownFetch()
                }




            },
            // 需要下载码
            hasDownFetch(code) {
                let url = this.getRequest('execUrl')
                if (code) url = url + '?downCode=' + code

                $.ajax({
                    url,
                    type: 'GET'
                }).done(res => {
                    console.log(res)

                    if (res.code != 0) return alert(res.message)
                    this.isDownInput = false
                    t = setInterval(() => {
                        this.getStatus(res)
                    }, 1000);
                })
            },
            // 查询状态
            getStatus(data) {
                $.ajax({
                    url: data.statusUrl + this.getRequest('statusId'),
                    // url: 'http://183.162.157.229:8081/distribute/getStatusV1?statusId=5',
                    type: 'GET'
                }).done(res => {
                    console.log(res)
                    if (res.code != 0) return alert(res.message)


                    if (res.data.status == '没有可用的证书') {
                        alert(res.data.status)
                        this.notdownLoad = false
                        clearInterval(t)
                        return false
                    }

                    if (res.data.status == '点击下载') {
                        clearInterval(t)
                        window.location = res.data.plist
                    }
                })
            },
            // 验证下载码
            vefiyCode() {
                if (this.code.length <= 0) return alert('请输入下载码')

                this.hasDownFetch(this.code)

            },
            // 跳转购买下载码
            pay() {
                window.open(this.base64Data.buyDownCodeUrl, '_blank')

            },
            //获取地址栏里（URL）传递的参数
            getRequest(value) {
                //url例子：www.bicycle.com?id="123456"&Name="bicycle"；
                var url = decodeURI(location.search); //?id="123456"&Name="bicycle";
                var object = {};
                if (url.indexOf("?") != -1) //url中存在问号，也就说有参数。
                {
                    var str = url.substr(1); //得到?后面的字符串
                    var strs = str.split("&"); //将得到的参数分隔成数组[id="123456",Name="bicycle"];
                    for (var i = 0; i < strs.length; i++) {
                        object[strs[i].split("=")[0]] = strs[i].split("=")[1]
                    }
                }
                return object[value];
            },
            // 获取应用信息
            getInfo() {
                // 第一次进入
                if (!this.getRequest('id')) {
                    // 重定向
                    if (this.getRequest('info')) {
                        this.getdownCode()
                    }
                } else {
                    $.ajax({
                        url: baseUrl + '/distribute/down/v1/' + this.getRequest('id'),
                        type: 'GET',
                    }).done(function(res) {
                        if (res.code != 0) return alert('ERROR 404')
                        console.log(res)

                        this.createShowData({
                            ...res.data,
                            ...res
                        })
                    })
                }
            },
            // 渲染页面
            createShowData(res) {
                this.languages = language[res.language]

                this.downloadData = res
                this.pro = res.pro
                document.title = res.appName



                jQuery('#output').qrcode(res.url);
                // $(".down").attr("href", res.ipa);
                // $(".down").click(function (event) {
                // 	console.log('1')
                // 	setTimeout(function () {
                // 		if (confirm) {
                // 			console.log('2')
                // 			location.href = res.pro;
                // 		}
                // 	}, 4500)
                // });

                //判断设备是否为iPhone
                if (/(iPhone|iPad|iPod|iOS)/i.test(ua)) {
                    if (Sys.safari) {
                        $(".down").attr("href", res.ipa);
                        $(".down").click(function(event) {
                            setTimeout(function() {
                                if (confirm) {
                                    location.href = res.pro;
                                }
                            }, 4500)
                        });
                        //打开引导弹窗
                        $(".doubt").click(function(event) {
                            $(".pup").fadeIn();
                            var swiper = new Swiper('.swiper-container', {
                                loop: true,
                                pagination: {
                                    el: '.swiper-pagination',
                                },
                            });
                        });
                    } else {
                        $("body").click(function(event) {
                            $(".pupPic").show();
                        });
                    }
                }
                //判断是否QQ内置浏览器
                else if (ua.indexOf(' qq') > -1 && ua.indexOf('mqqbrowser') < 0) {
                    document.getElementById('fz').style.display = 'block';
                    $(".down").attr("href", '###');
                    $("body").click(function(event) {
                        $(".pupPic").show();
                    });
                }
                //判断Android
                else if (/(Android)/i.test(ua)) {
                    $(".down").attr("href", res.apk);
                    //打开引导弹窗
                    $(".doubt").click(function(event) {
                        $(".pup").fadeIn();
                        var swiper = new Swiper('.swiper-container', {
                            loop: true,
                            pagination: {
                                el: '.swiper-pagination',
                            },
                        });
                    });
                }
                //在微信中打开
                if (ua.match(/MicroMessenger/i) == "micromessenger") {
                    document.getElementById('fz').style.display = 'block';
                    $(".down").attr("href", '###');
                    $("body").click(function(event) {
                        $(".pupPic").show();
                    });
                }

            }
        },
        created() {
            this.getInfo()




        },

    })


    // function tishi(s) {
    // 	if (s > 0) {
    // 		// document.getElementById('kai').style.display = 'block';
    // 		$('#kai').css('display','block')
    // 	} else {
    // 		// document.getElementById('kai').style.display = 'none';
    // 		$('#kai').css('display','none')
    // 	}
    // }
    if (!/windows phone|iphone|android/ig.test(window.navigator.userAgent)) {
        document.getElementById('pc').style.display = 'block';
    } else {
        document.getElementById('m').style.display = 'block';
    }







    //关闭弹窗
    $(".colse").click(function(event) {
        $(".pup").fadeOut();
    });

    var s = document.body.clientWidth;
    if (s < 500) {
        document.getElementById("w").style.backgroundSize = "1500px auto";
    }



    // translate()
</script>


</body>

</html>