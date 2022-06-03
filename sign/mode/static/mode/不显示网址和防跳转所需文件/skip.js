<script type="text/javascript">
//iOS Web APP中点击链接跳转到Safari 浏览器新标签页的问题 devework.com
//stanislav.it/how-to-prevent-ios-standalone-mode-web-apps-from-opening-links-in-safari
if(("standalone" in window.navigator) && window.navigator.standalone){
    var noddy, remotes = false;
    document.addEventListener('click', function(event) {
        noddy = event.target;
        while(noddy.nodeName !== "A" && noddy.nodeName !== "HTML") {
            noddy = noddy.parentNode;
        }
        if('href' in noddy && noddy.href.indexOf('http') !== -1 && (noddy.href.indexOf(document.location.host) !== -1 || remotes))
        {
            event.preventDefault();
            document.location.href = noddy.href;
        }
    },false);
}
</script>