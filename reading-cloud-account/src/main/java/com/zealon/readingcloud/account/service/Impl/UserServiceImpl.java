package com.zealon.readingcloud.account.service.Impl;

import com.zealon.readingcloud.account.bo.UserBO;
import com.zealon.readingcloud.account.dao.UserMapper;
import com.zealon.readingcloud.account.service.UserService;
import com.zealon.readingcloud.account.utils.JwtUtil;
import com.zealon.readingcloud.account.utils.UserUtil;
import com.zealon.readingcloud.account.vo.AuthVO;
import com.zealon.readingcloud.account.vo.UserVO;
import com.zealon.readingcloud.common.pojo.account.User;
import com.zealon.readingcloud.common.result.Result;
import com.zealon.readingcloud.common.result.ResultUtil;
import com.zealon.readingcloud.common.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Calendar;
import java.util.Date;

import static com.zealon.readingcloud.common.constant.JwtConstant.EXPIRE_DAY;

/**
 * 用户服务
 * @author hasee
 */
@Service
public class UserServiceImpl implements UserService {

    private static  final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result register(UserBO userBO) {

        User dbUser = userMapper.selectByLoginName(userBO.getLoginName());

        if(dbUser != null){

            return ResultUtil.verificationFailed().buildMessage(userBO.getLoginName() + "已存在，请使用其他登录名进行注册");

        }

        User user = new User();
        BeanUtils.copyProperties(userBO, user);

        String encryptPwd = UserUtil.getUserEncryptPassword(userBO.getLoginName(), userBO.getUserPwd());
        user.setUserPwd(encryptPwd);

        try{

            user.setHeadImgUrl("http://reading.zealon.cn/default.jpg");
            user.setUuid(CommonUtil.getUUID());
            userMapper.insert(user);

        }catch (Exception e){

            LOGGER.error("注册用户失败! {}; user:{}",e,user);
            return ResultUtil.fail().buildMessage("注册失败");

        }

        return ResultUtil.success().buildMessage("注册成功！请登录");
    }

    @Override
    public Result<AuthVO> login(String loginName, String password) {

        try {

            User user = userMapper.selectByLoginName(loginName);
            if (null == user) {

                return ResultUtil.notFound().buildMessage("登录失败！用户不存在");
            }

            //校验用户密码
            String encryptPwd = UserUtil.getUserEncryptPassword(loginName, password);
            if(!user.getUserPwd().equals(encryptPwd)){

                return ResultUtil.verificationFailed().buildMessage("登陆失败，请检查密码");
            }

            //验证成功，返回用户信息
            AuthVO vo = new AuthVO();
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            vo.setToken(JwtUtil.buildJwt(this.getLoginExpire(), userVO));
            vo.setUser(userVO);

            return ResultUtil.success(vo);

        } catch (Exception e) {
            LOGGER.error("登陆失败了！{}; loginName:{}",e,loginName);
            return ResultUtil.fail().buildMessage("登录失败！");
        }

    }

    @Override
    public Result logout(String loginName) {
        return null;
    }

    @Override
    public Result update(UserBO userBO) {
        return null;
    }

    @Override
    public Result updatePassword(UserBO userBO) {
        return null;
    }


    /**
     * 获取登录过期时间
     * @return
     */
    private Date getLoginExpire(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, EXPIRE_DAY);
        return calendar.getTime();
    }

}
