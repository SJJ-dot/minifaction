function http(obj) {
  wx.request({
    url: obj.url,
    data: obj.data,
    success: function (res) {
      if (res.statusCode == 200 && res.data.status == 1) {
        obj.success(res.data.data);
      } else {
        var errMsg = res.data.status + ":" + res.data.errorMsg;
        if (obj.reject !== undefined)
          obj.reject(errMsg);
        if (obj.fail !== undefined)
          obj.fail(errMsg);
        wx.showToast({
          title: errMsg,
          icon: 'none',
          duration: 2000
        });
      }
    },
    fail: function (res) {
      if (obj.fail !== undefined)
        obj.fail(res.errMsg);
      wx.showToast({
        title: res.errMsg,
        icon: 'none',
        duration: 2000
      });
    }
  })
}

function getGBookKey(item) {
  return "book:" + item.name + "-author:" + item.author
}
var regStr = "(http[s]?://([a-zA-Z\\d]+\\.)+[a-zA-Z\\d]+)/?"
function getDomain(url){
  return url.match(regStr)[1]
}
module.exports = {
  http: http,
  getGBookKey: getGBookKey,
  getDomain:getDomain,
}
