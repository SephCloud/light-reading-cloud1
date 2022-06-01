package com.zealon.readingcloud.homepage.service.Impl;

import com.github.pagehelper.PageHelper;
import com.zealon.readingcloud.common.cache.RedisExpire;
import com.zealon.readingcloud.common.cache.RedisHomepageKey;
import com.zealon.readingcloud.common.cache.RedisService;
import com.zealon.readingcloud.common.pojo.index.IndexPageConfig;
import com.zealon.readingcloud.common.result.Result;
import com.zealon.readingcloud.common.result.ResultUtil;
import com.zealon.readingcloud.common.utils.CommonUtil;
import com.zealon.readingcloud.homepage.dao.IndexPageConfigMapper;
import com.zealon.readingcloud.homepage.service.IndexBannerService;
import com.zealon.readingcloud.homepage.service.IndexBookListItermService;
import com.zealon.readingcloud.homepage.service.IndexBookListService;
import com.zealon.readingcloud.homepage.service.IndexPageConfigService;
import com.zealon.readingcloud.homepage.vo.IndexPageVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 精品页服务
 * @author hasee
 */
@Service
public class IndexPageConfigServiceImpl implements IndexPageConfigService {

    private static  final Logger LOGGER = LoggerFactory.getLogger(IndexPageConfigServiceImpl.class);

    @Autowired
    private IndexPageConfigMapper indexPageConfigMapper;

    @Autowired
    private IndexBannerService indexBannerService;

    @Autowired
    private IndexBookListService indexBookListService;

    @Autowired
    private RedisService redisService;


    /**
     *返回精品页列表
     * @param type 页面类型（1.精品页 2.男生 3.女生）
     * @param page 页数
     * @param limit 每页数量
     * @return
     */
    @Override
    public Result getIndexPageByType(Integer type, Integer page, Integer limit) {

        //对于每一种类型，生成不同的key
        String key = RedisHomepageKey.getHomepageKey(type);

        //对于相应类型的不同页数，取到对应的IndexPageVO列表
        List<IndexPageVO> pageVOS = redisService.getHashListVal(key, page.toString(), IndexPageVO.class);

        //如果缓存中存在，直接返回
        if(pageVOS != null){
            return ResultUtil.success(pageVOS);
        }

        //获取精品页配置
        List<IndexPageConfig> pageConfigs = getIndexPageWithPaging(type, page, limit);

        if(CommonUtil.isEmpty(pageConfigs)){
            LOGGER.warn("当前请求页没有配置项！");
            return ResultUtil.success(new ArrayList<>()).buildMessage("当前请求页没有配置项");
        }

        //设pageVOS的大小为pageConfigs.size
        pageVOS = new ArrayList<>(pageConfigs.size());

        for(int i = 0; i < pageConfigs.size(); i++){

            IndexPageConfig pageConfig = pageConfigs.get(i);
            IndexPageVO vo = new IndexPageVO();
            BeanUtils.copyProperties(pageConfig, vo);

            //模块是否有效
            boolean okFlag = true;
            switch (pageConfig.getItemType()){

                case 1:
                    //书单
                    //通过IndexPageConfig中的ItemId得到BookListId，从而得到相应的BookList对象
                    //再将得到的BookList对象放入VO中
                    vo.setBookList(indexBookListService.getIndexBookListVO(pageConfig.getItemId(), null));
                    if(vo.getBookList() == null){

                        okFlag = false;
                    }
                    break;

                case 2:
                    //Banner
                    vo.setBanner(indexBannerService.getBannerVO(pageConfig.getItemId()));
                    if(vo.getBanner() == null){
                        okFlag = false;
                    }
                    break;

                default:
                    break;
            }

            //对应模块值不为空，才进行添加到VOS中
            if(okFlag){
                pageVOS.add(vo);
            }
        }

        if(pageVOS.size() > 0){

            redisService.setHashValExpire(key, page.toString(), pageVOS, RedisExpire.DAY);
        }


        return ResultUtil.success(pageVOS);
    }


    /**
     * 分页获取精品页配置列表
     * @param type 页面类型：1.home,2.男生,3.女生
     * @param page
     * @param limit
     * @return
     */
    private List<IndexPageConfig> getIndexPageWithPaging(Integer type, Integer page, Integer limit){

        if(page <= 0){
            page = 1;
        }

        PageHelper.startPage(page, limit);
        //PageHelper.startPage方法，紧跟在这个方法后的MyBatis查询方法会被进行分页
        List<IndexPageConfig> pageWithResult = indexPageConfigMapper.findPageWithResult(type);

        return pageWithResult;
    }
}
