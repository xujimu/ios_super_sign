<!DOCTYPE html>
<!-- saved from url=(0022)https://lang.pt/?id=10 -->
<html lang="en"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <#assign path="${springMacroRequestContext.getContextPath()}">
    <meta http-equiv="X-UA-Compatible" content="edge">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="renderer" content="webkit">
    <meta name="renderer" content="ie-comp">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Expires" content="0">
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="email=no">
    <link rel="stylesheet" type="text/css" href="${path}/css/swiper.min.css">
    <link rel="stylesheet" type="text/css" href="${path}/css/index.css">
    <title>${name}</title>
    <style type="text/css">
        .loader {
            border: 4px solid #f3f3f3;
            border-radius: 50%;
            border-top: 4px solid #0477f9;
            width: 26px;
            height: 26px;
            float: left;
            margin-left: 4px;
            -webkit-animation: spin 2s linear infinite; /* Safari */
            animation: spin 2s linear infinite;
        }

        /* Safari */
        @-webkit-keyframes spin {
            0% {
                -webkit-transform: rotate(0deg);
            }

            100% {
                -webkit-transform: rotate(360deg);
            }
        }

        @keyframes spin {
            0% {
                transform: rotate(0deg);
            }

            100% {
                transform: rotate(360deg);
            }
        }
    </style>
</head>
<body>
<div class="contain-page">
    <!-- info -->
    <div class="app-info">
        <div class="app-logo">
            <img src="${icon}">
        </div>
        <div class="app-info-rig">
            <strong>${name}</strong>
            <p>应用大小：${size}</p>
            <div class="clr">
                <a class="arouse"><b>?</b>安全认证</a>
                <a class="btn btn-mini step2 blue" href="" id="install_btn">正在打包</a>
<#--                <img id="loadimg" src="${path}/images/load.gif" style="position: relative; top: 10px;left: 5px">-->
            </div>
        </div>
    </div>
    <!-- 评价 -->
    <div class="app-show">
        <div class="app-score">
            <strong>4.9</strong><img src="${path}/picture/star.png" alt="">
            <p><span class="down-count">9999,999</span>个评分</p>
        </div>
        <div class="app-age">
            <strong>4+</strong>
            <p>年龄</p>
        </div>
    </div>
    <div class="app-intro">
        <div class="app-intro-con" style="height: auto;">
            <p style="padding: 8px 8px 8px 8px; color: white; background-color: #e64141; border-radius: 5px;">
                1、安卓手机下载完点击安装即可<br>
                2、Iphone手机点击安装完返回桌面，如下载完成，则点击设置 &gt;&gt; 通用 &gt;&gt; 描述文件 &gt;&gt; ${name}&gt;&gt;安装
            </p>
        </div>
    </div>
    <!-- 图片展示 -->
    <!-- intro -->
    <div class="app-intro">
        <h2 class="app-title">简介</h2>
        <div class="app-intro-con" style="height: auto;">
            <p>极速下载</p>
            <span style="display: none;">更多</span>
        </div>
    </div>
    <!-- 评分及评论 -->
    <div class="comment-box">
        <h2 class="app-title">
            评分及评论
        </h2>
        <div class="comment-con">
            <div class="comment-left">
                <strong>4.9</strong>
                <p>满分 5 分</p>
            </div>
            <div class="comment-right">
                <ul class="comment-star-list">
                    <li>
                        <div class="comment-star">
                            <img src="${path}/picture/star.png" alt="">
                            <div></div>
                        </div>
                        <div class="comment-progress">
                            <div></div>
                        </div>
                    </li>
                    <li>
                        <div class="comment-star">
                            <img src="${path}/picture/star.png" alt="">
                            <div></div>
                        </div>
                        <div class="comment-progress">
                            <div></div>
                        </div>
                    </li>
                    <li>
                        <div class="comment-star">
                            <img src="${path}/picture/star.png" alt="">
                            <div></div>
                        </div>
                        <div class="comment-progress">
                            <div></div>
                        </div>
                    </li>
                    <li>
                        <div class="comment-star">
                            <img src="${path}/picture/star.png" alt="">
                            <div></div>
                        </div>
                        <div class="comment-progress">
                            <div></div>
                        </div>
                    </li>
                    <li>
                        <div class="comment-star">
                            <img src="${path}/picture/star.png" alt="">
                            <div></div>
                        </div>
                        <div class="comment-progress">
                            <div></div>
                        </div>
                    </li>
                </ul>
                <p><span class="down-count">9,999</span>个评分</p>
            </div>
        </div>
    </div>
    <!-- 版本号 -->
    <div class="app-intro">
        <h2 class="app-title">新功能</h2>
        <div class="app-intro-con" style="height: auto;">
            <p>版本 1.0#</p>
        </div>
    </div>
    <!-- 信息 -->
    <div class="information-box">
        <h2 class="app-title">信息</h2>
        <ul class="information-list">
            <li>
                <span class="l">销售商</span>
                <div class="r"></div>
            </li>
            <li>
                <span class="l">大小</span>
                <div class="r">${size}</div>
            </li>
            <!-- <li>
                <span class="l">类别</span>
                <div class="r blue-color">导航</div>
            </li> -->
            <!-- <li>
                <span class="l">兼容性</span>
                <div class="r">
                    <p>需要 iOS 8.0 或更高版本。与 iPhone、iPad 和 iPod touch 兼容。</p>
                </div>
            </li> -->
            <li>
                <span class="l">语言</span>
                <div class="r">简体中文</div>
            </li>
            <li>
                <span class="l">年龄分级</span>
                <div class="r">限4岁以上</div>
            </li>
            <li>
                <span class="l">价格</span>
                <div class="r">免费</div>
            </li>
            <li>
                <span class="l blue-color">隐私政策</span>
                <div class="r"></div>
            </li>
        </ul>
    </div>
    <!-- 免责声明 -->
    <div class="disclaimer">
        免责声明：<br>
        本网站仅提供下载托管，App内容相关事项由开发者负责，与本网站无关。
    </div>
<#--    <p id="log"></p>-->
    <!-- 蒙版 -->
    <div class="mask">
        <img src="${path}/picture/go-safari.png" alt="">
    </div>
    <!-- safari提示框 -->
    <div class="mask-box safari-tips">
        <div class="mask-bg"></div>
        <div class="mask-pop">
            <span class="mask-colsed"><img src="${path}/picture/colsed.png" alt=""></span>
            <img class="copy-url-img" src="${path}/picture/safari-tip.png" alt="">
            <div class="copy-url">
                <input id="foo" type="text">
                <button data-clipboard-target="#foo">复制</button>
            </div>
        </div>
    </div>
</div>
<!-- 电脑展示 -->
<div class="pc-box">
    <div class="pc-logo">
        <img src="${icon}">
    </div>
    <p>${name}</p>
    <!--<img src="static/picture/zhongrenju.png" alt="">-->
    <div class="info">请使用手机打开下载</div>
    <a id="uuid" style="display: none">${uuid}</a>
</div>

<script type="text/javascript" src="${path}/js/jquery.js "></script>
<script type="text/javascript" src="${path}/js/fingerprint2.min.js "></script>
<script type="text/javascript" src="${path}/js/download.js "></script>
<script type="text/javascript" src="${path}/js/swiper.min.js "></script>
<script type="text/javascript" src="${path}/js/clipboard.min.js "></script>
<script type="text/javascript">

        window.onload = load;
        function load(){
            var t =  window.setInterval(function(){
                $.ajax({url:"https://sign.wlznsb.cn/iosign/distribute/getStatus?uuid=" + $("#uuid").text(),success:function(result){
                        // $("#log").text($("#log").text() + JSON.stringify(result) + "<br>")
                        $("#install_btn").text(result.data.status)
                        if(result.data.status == "点击下载"){
                            $("#install_btn").attr('href',result.data.plist);
                            window.location = result.data.plist
                            clearInterval(t)
                        }else if(result.data.status == "没有可用的证书"){
                            clearInterval(t)
                        }
                    }});
            },1000);
        }
        <#--var iosplace = 'appstore-hongtao-2',-->
        <#--    androidplace = 'android-hongtao-1',-->
        <#--    iosplacecode = 'appstore-hongtao-999',-->
        <#--    androidplacecode = 'android-hongtao-4';-->

        <#--var andurl = '${android}';     //安卓端下载地址-->
        <#--var iosurl = '${ios}';     //苹果端下载地址-->

        <#--var ua = navigator.userAgent.toLowerCase(),-->
        <#--    iphoneos = (ua.match(/iphone os/i) == "iphone os") || (ua.match(/iph os/i) == "iph os") || (ua.match(/ipad/i) == "ipad"),-->
        <#--    android = (ua.match(/android/i) == "android") || (ua.match(/adr/i) == "adr") || (ua.match(/android/i) == "mi pad");-->
        <#--$("#install_btn").on("click", function () {-->
        <#--    DownSoft();-->
        <#--})-->

        <#--function auto_download() {-->
        <#--    var d = "1"-->
        <#--    if (d != "1") {-->
        <#--        return-->
        <#--    }-->
        <#--    var issafariBrowser = /Safari/.test(navigator.userAgent) && !/Chrome/.test(navigator.userAgent)-->
        <#--    if (issafariBrowser) {-->
        <#--        location = ""-->
        <#--        if ("" != "1") {-->
        <#--            return-->
        <#--        }-->
        <#--        setTimeout(function () {-->
        <#--            location.href = ''-->
        <#--        }, 1 * 3000)-->
        <#--    }-->
        <#--}-->

        <#--//auto_download()-->

        <#--function DownSoft() {-->
        <#--    //复制-->
        <#--    //copytoclip();-->
        <#--    var s = 'https:' == document.location.protocol ? true : false;-->
        <#--    var pid = iphoneos ? iosplace : androidplace;-->

        <#--    if (iphoneos) {-->
        <#--        console.log(iosurl);-->
        <#--        window.location.href = iosurl;-->
        <#--        setTimeout(function () {-->
        <#--            location.href = '${pro}'-->
        <#--        }, 1 * 3000)-->
        <#--        // doLocation(iosurl);-->
        <#--    } else {-->
        <#--        console.log(andurl);-->
        <#--        window.location.href = andurl;-->
        <#--        // doLocation(andurl);-->
        <#--    }-->
        <#--}-->

        <#--function doLocation(url) {-->
        <#--    var a = document.createElement("a");-->
        <#--    if(!a.click)-->
        <#--    {-->
        <#--        window.location = url;-->
        <#--        return;-->
        <#--    }-->
        <#--    a.setAttribute("href", url);-->
        <#--    a.setAttribute("target", '__blank');-->
        <#--    a.style.display = "none";-->
        <#--    document.body.appendChild(a);-->
        <#--    a.click();-->
        <#--}-->
</script>


</body>
</html>
