package com.zealon.readingcloud.homepage.service.Impl;

import com.zealon.readingcloud.common.cache.RedisExpire;
import com.zealon.readingcloud.common.cache.RedisHomepageKey;
import com.zealon.readingcloud.common.cache.RedisService;
import com.zealon.readingcloud.common.pojo.index.IndexBanner;
import com.zealon.readingcloud.common.pojo.index.IndexBannerItem;
import com.zealon.readingcloud.common.utils.CommonUtil;
import com.zealon.readingcloud.homepage.dao.IndexBannerItemMapper;
import com.zealon.readingcloud.homepage.dao.IndexBannerMapper;
import com.zealon.readingcloud.homepage.service.IndexBannerService;
import com.zealon.readingcloud.homepage.vo.BannerItemVO;
import com.zealon.readingcloud.homepage.vo.IndexBannerVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Banner 服务
 * @author hasee
 */
@Service
public class IndexBannerServiceImpl implements IndexBannerService {

    @Autowired
    private IndexBannerMapper indexBannerMapper;

    @Autowired
    private IndexBannerItemMapper indexBannerItemMapper;

    @Autowired
    private RedisService redisService;


    @Override
    public IndexBannerVO getBannerVO(Integer bannerId) {
        //通过bannerId查找IndexBanner
        //IndexBanner中


        //生成缓存key
        String key = RedisHomepageKey.getBannerVoKey(bannerId);

        //通过key查找缓存中是否存在该Banner
        IndexBannerVO vo = redisService.getCache(key, IndexBannerVO.class);


        //若缓存中存在则直接返回
        if(vo != null){
            return vo;
        }


        //若缓存中不存在则在DB中查询Banner
        IndexBanner indexBanner = indexBannerMapper.selectById(bannerId);
        //DB查询不存在则返回空
        if(indexBanner == null){
            return null;
        }

        //通过BannerId查询Banner中的BannerItem
        List<IndexBannerItem> bannerItems = indexBannerItemMapper.findPageWithResult(bannerId);

        //如果Banner中不存在Item，则返回空
        if(CommonUtil.isEmpty(bannerItems)){
            return null;
        }

        List<BannerItemVO> items = new ArrayList<>();

        //遍历BannerList
        for(int i = 0; i < bannerItems.size(); i++){

            BannerItemVO item = new BannerItemVO();
            IndexBannerItem banner = bannerItems.get(i);

            //将BannerItem中的一些值复制到BannerItemVO中
            BeanUtils.copyProperties(banner, item);
            items.add(item);

        }

        vo = new IndexBannerVO(indexBanner.getId(), indexBanner.getName(), items);


        //放入缓存，设置过期时间
        redisService.setExpireCache(key, vo, RedisExpire.HOUR_FOUR);

        return vo;
    }
}
