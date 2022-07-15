using System;
using UnityEngine;
namespace FusionSDK.Core
{ 
    /// <summary>
    /// 用来接收Android端的回调
    /// </summary>
    public class FusionCallback : MonoBehaviour {
        void Awake()
        {
            DontDestroyOnLoad(gameObject);
            Instance = this;
        }

        public static FusionCallback Instance { get; private set; }


        public Action onInitSucceedHandle;
        public Action<string> onInitFailedHandle;

        public Action<string> onLoginSucceedHandle;
        public Action<string> onLoginFailedHandle;
        public Action onLoginCanceledHandle;

        public Action onLogoutSucceedHandle;
        public Action<string> onLogoutFailedHandle;
        public Action onLogoutCanceledHandle;

        public Action onExitSucceedHandle;
        public Action onExitCanceledHandle;

        public Action<string> onPaySucceedHandle;
        public Action<string> onPayFailedHandle;
        public Action onPayCanceledHandle;

        public Action<string> onPushDataGetSucceedHandle;
        public Action<string> onPushDataGetFailedHandle;

        public Action onSubmitUserInfoSucceedHandle;
        public Action<string> onSubmitUserInfoFailedHandle;

        public Action<string> onCertificationInfoGetSucceedHandle;
        public Action<string> onCertificationInfoGetFailedHandle;

        public Action onShareSucceedHandle;
        public Action<string> onShareFailedHandle;
        
        /// <summary>
        /// 进入游戏调用登录
        /// </summary>
        public void onInitSucceed()
        {
            log("初始化成功");
            if (null != onInitSucceedHandle)
            {
                onInitSucceedHandle();
            }
        }

        /// <summary>
        /// 初始化失败游戏
        /// </summary>
        /// <param name="msg">Message.</param>
        public void onInitFailed(string msg)
        {
            log("初始化失败：" + msg);
            if (null != onInitFailedHandle)
            {
                onInitFailedHandle(msg);
            }
        }

        /// <summary>
        /// 登录成功
        /// </summary>
        /// <param name="data">data.</param>
        public void onLoginSucceed(string data)
        {
            log("账号登录成功 data:" + data);

            if (null != onLoginSucceedHandle)
            {
                onLoginSucceedHandle(data);
            }
        }

        /// <summary>
        /// 登录界面退出，返回到游戏画面
        /// </summary>
        /// <param name="msg">Message.</param>
        public void onLoginFailed(string msg)
        {
            log("账号登录失败：" + msg);

            if (null != onLoginFailedHandle)
            {
                onLoginFailedHandle(msg);
            }
        }

        /// <summary>
        /// 登录界面退出，返回到游戏画面
        /// </summary>
        public void onLoginCanceled()
        {
            if (null != onLoginCanceledHandle)
            {
                onLoginCanceledHandle();
            }
        }

        /// <summary>
        /// 当前登录用户已退出，应将游戏切换到未登录的状态。
        /// </summary>
        public void onLogoutSucceed()
        {
            log("账号退出成功");

            if (null != onLogoutSucceedHandle)
            {
                onLogoutSucceedHandle();
            }
        }

        /// <summary>
        /// 登录失败
        /// </summary>
        /// <param name="msg">Message.</param>
        public void onLogoutFailed(string msg)
        {
            log("账号退出失败：" + msg);

            if (null != onLogoutFailedHandle)
            {
                onLogoutFailedHandle(msg);
            }
        }

        public void onLogoutCanceled()
        {
            log("账号退出成功");

            if (null != onLogoutCanceledHandle)
            {
                onLogoutCanceledHandle();
            }
        }
        
        /// <summary>
        /// 退出游戏成功
        /// </summary>
        public void onExitSucceed()
        {
            log("退出游戏");

            if (null != onExitSucceedHandle)
                onExitSucceedHandle();
            Application.Quit();
        }

        /// <summary>
        /// 用户取消退出游戏
        /// </summary>
        /// <param name="msg">Message.</param>
        public void onExitCanceled()
        {
            log("取消退出游戏");
            if (null != onExitCanceledHandle)
            {
                onExitCanceledHandle();
            }
        }

        /// <summary>
        /// 创建订单成功
        /// </summary>
        /// <param name="orderInfo">Order info.</param>
        public void onPaySucceed(string orderInfo)
        {
            log(orderInfo);

            if (null != onPaySucceedHandle)
            {
                onPaySucceedHandle(orderInfo);
            }
        }

        /// <summary>
        /// 用户取消订单支付
        /// </summary>
        /// <param name="orderInfo">Order info.</param>
        public void onPayFailed(string orderInfo)
        {
            log(orderInfo);

            if (null != onPayFailedHandle)
            {
                onPayFailedHandle(orderInfo);
            }
        }

        public void onPayCanceled()
        {
            log("取消支付");
            if (null != onPayCanceledHandle)
            {
                onPayCanceledHandle();
            }
        }

        public void onPushDataGetSucceed(string data)
        {
            if (null != onPushDataGetSucceedHandle)
            {
                onPushDataGetSucceedHandle(data);
            }
        }

        public void onPushDataGetFailed(string info)
        {
            if (null != onPushDataGetFailedHandle)
            {
                onPushDataGetFailedHandle(info);
            }
        }

        /// <summary>
        /// 上传角色信息成功
        /// </summary>
        /// <param name="msg"></param>
        public void onSubmitUserInfoSucceed()
        {
            log("上传角色信息成功！");
            if (null != onSubmitUserInfoSucceedHandle)
            {
                onSubmitUserInfoSucceedHandle();
            }
        }

        /// <summary>
        /// 上传角色信息失败
        /// </summary>
        /// <param name="msg"></param>
        public void onSubmitUserInfoFailed(string msg)
        {
            log("上传角色信息失败：" + msg);
            if (null != onSubmitUserInfoFailedHandle)
            {
                onSubmitUserInfoFailedHandle(msg);
            }
        }

        /// <summary>
        /// 获取防沉迷信息成功
        /// </summary>
        /// <param name="info"></param>
        public void onCertificationInfoGetSucceed(string info)
        {
            log("防沉迷信息：" + info);
            if (onCertificationInfoGetSucceedHandle != null)
            {
                onCertificationInfoGetSucceedHandle(info);
            }
        }

        /// <summary>
        /// 获取防沉迷信息失败
        /// </summary>
        /// <param name="info"></param>
        public void onCertificationInfoGetFailed(string msg)
        {
            log("获取防沉迷失败：" + msg);
            if (onCertificationInfoGetFailedHandle != null)
            {
                onCertificationInfoGetFailedHandle(msg);
            }
        }

        private void log(string msg)
        {
            Debug.Log(msg);
        }

        public void onShareSucceed()
        {
            Debug.LogError("分享成功！!!!");
            if (null != onShareSucceedHandle)
                onShareSucceedHandle();
        }

        public void onShareFailed(string data)
        {
            Debug.LogError(data);
            if (null != onShareFailedHandle)
                onShareFailedHandle(data);
        }
    }
}