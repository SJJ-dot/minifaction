// pages/read/read.js
var WxParse = require('../../wxParse/wxParse.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    book: {},
    success:false,
  },

  onLoad: function (options) {

    var that = this;
    /**
     * 初始化emoji设置
     */
    WxParse.emojisInit('[]', "/wxParse/emojis/", {
      "00": "00.gif",
      "01": "01.gif",
      "02": "02.gif",
      "03": "03.gif",
      "04": "04.gif",
      "05": "05.gif",
      "06": "06.gif",
      "07": "07.gif",
      "08": "08.gif",
      "09": "09.gif",
      "09": "09.gif",
      "10": "10.gif",
      "11": "11.gif",
      "12": "12.gif",
      "13": "13.gif",
      "14": "14.gif",
      "15": "15.gif",
      "16": "16.gif",
      "17": "17.gif",
      "18": "18.gif",
      "19": "19.gif",
    });

    var page = this;
    wx.getStorage({
      key: options.url,
      success: function (res) {
        var index = options.index || res.data.index;
        res.data.index = index;
        page.setData({
          success:false,
          book: res.data,
          name: res.data.chapterList[index].chapterName,
        })
        page.showChapter(index);
      }
    })
  },

  onHide: function () {
    this.saveState()
  },
  onUnload: function () {
    this.saveState()
  },
  onPullDownRefresh: function () {
    if (this.data.success) {
      this.showChapter(this.data.book.index - 1)
    } else {
      this.showChapter(this.data.book.index)
    }
  },
  onReachBottom: function () {
    if (this.data.success) {
      this.showChapter(this.data.book.index + 1)
    } else {
      this.showChapter(this.data.book.index)
    }
  },
  showChapter: function (index) {
    var page = this;
    if (index < 0 || index >= page.data.book.chapterList.length)
      return
    var chapter = page.data.book.chapterList[index];
    page.data.book.index = index;
    page.saveState();
    page.setData({
      success : false,
      name: chapter.chapterName,
      content: "正在加载……"
    });
    wx.getStorage({
      key: chapter.url,
      success: function (res) {
        WxParse.wxParse('article', 'html', res.data.content, page, 5);
        page.setData({
         success :true,
         content: "加载完成"
        })
        wx.stopPullDownRefresh()
      },
      fail:function(){
        wx.util.http({
          url: getApp().data.baseUrl+"/chapter?url="+chapter.url,
          success:function(res){
            WxParse.wxParse('article', 'html', res.content, page, 5);
            page.setData({
              success: true,
              content: "加载完成"
            })
            wx.setStorage({
              key: res.url,
              data: res,
            })
            wx.stopPullDownRefresh()
          },
          fail:function(){
            page.setData({
              success: false,
              content : "章节内容加载失败"
            })
            wx.stopPullDownRefresh()
          }
        })
      }
    })
  },
  saveState: function () {
    var page = this;
    wx.setStorage({
      key: page.data.book.url,
      data: page.data.book,
    })
  }
})