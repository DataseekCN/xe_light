/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑, 永无BUG!
 * 　　　　 ┃　　　┃Code is far away from bug with the animal protecting
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 * Module Desc:
 * User: taosm
 * DateTime: 2019-09-25 15
 */
package com.dataseek.xe.service.base.impl;

import com.dataseek.xe.dao.IOauthDao;
import com.dataseek.xe.entity.XeroDeveloperDetail;
import com.dataseek.xe.service.base.IInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class InitService implements IInitService {
    @Autowired
    private IOauthDao oauthDao;

    @Override
    public XeroDeveloperDetail InitXeroDeveloperDetail(){
        XeroDeveloperDetail xeroDeveloperDetail = oauthDao.queryXeroDeveloperDetail();
        return xeroDeveloperDetail;
    }
}
