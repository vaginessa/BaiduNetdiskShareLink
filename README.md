# BaiduNetdiskShareLink
Get the real download link given a baidu netdisk share page.

The `get-bd-download-link` function takes a baidu netdisk share link (it could be both desktop and mobile version) and returns the real download link of the shared file.

It will ask for a verification code if needed. `Shotwell` should be installed if you need this functionality to work.

Example:

```clojure
(get-bd-download-link
 "http://pan.baidu.com/s/1sJovW")
;= http://d.pcs.baidu.com/file/d9161974424d2450cb015d3765e4f83d?fid=1494862908-250528-194896851427267&time=1438965956&expires=1438967275&rt=sh&chkv=1&sign=FDTERVA-DCb740ccc5511e5e8fedcff06b081203-NCXjrLcXahY7fF3PSCyyIZcpVhA%3D&r=652616426&sharesign=chbAe5AZBgfJquAyee7rLnLraxKt0eaJAs+HHWsHLGuY/PkrktmbcFQQG+V14OWKqu7ui8zLffNBf0EIjZOVRJin1BAl/FVL/3GmzJEEiVPrRq4JERbuS4VJlCC5h6e3wpnMMvxIHC8=&sh=1
```
