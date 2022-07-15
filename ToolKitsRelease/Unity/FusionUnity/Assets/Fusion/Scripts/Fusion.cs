using LitJson;
using System.Collections.Generic;
using UnityEngine;

namespace Com.Magata.Fusion
{
    public class Fusion
    {
        private static AndroidJavaClass sdkClass = new AndroidJavaClass("com.magata.fusion.Fusion");

        /// <summary>
        /// SDK初始化
        /// </summary>
        /// <param name="jsonString"></param>
        public static void Init()
        {
            sdkClass.CallStatic("init", Utility.Utility.CurrentActivity);
        }

        /// <summary>
        /// 用户登录
        /// </summary>
        public static void Login()
        {
            if (null == sdkClass)
                return;
            sdkClass.CallStatic("login", Utility.Utility.CurrentActivity);
        }

        /// <summary>
        /// 用户登出
        /// </summary>
        public static void Logout()
        {
            if (null == sdkClass)
                return;
            sdkClass.CallStatic("logout", Utility.Utility.CurrentActivity);
        }

        /// <summary>
        /// 上传角色信息
        /// 调用时机：玩家登陆、创建角色、升级、改名、退出
        /// 
        /// 必填：
        /// 1.serverId:区服ID
        /// 2.serverName:区服名称
        /// 3.uid:角色id
        /// 4.name:角色名称
        /// 5.level:角色等级
        /// 6.createTimeStamp:角色创建时间
        /// </summary>
        /// <param name="jsonString"></param>
        public static void SubmitUserInfo(string jsonString)
        {
            if (null == sdkClass)
                return;
            sdkClass.CallStatic("submitUserInfo", Utility.Utility.CurrentActivity, jsonString);
        }

        /// <summary>
        /// 用户支付
        /// </summary>
        /// <param name="jsonString"></param>
        public static void Pay(string jsonString)
        {
            if (null == sdkClass)
                return;
            sdkClass.CallStatic("pay", Utility.Utility.CurrentActivity, jsonString);
        }

        /// <summary>
        /// 退出sdk
        /// 游戏退出时调用
        /// </summary>
        public static void Exit()
        {
            if (null == sdkClass)
                return;
            sdkClass.CallStatic("exit", Utility.Utility.CurrentActivity);
        }

        public static void GetCertificationInfo()
        {
            if (null == sdkClass)
                return;
            sdkClass.CallStatic("getCertificationInfo", Utility.Utility.CurrentActivity);
        }

        /// <summary>
        /// 检查掉单
        /// </summary>
        public static void CheckMissingOrder()
        {
            if (null == sdkClass)
                return;
            sdkClass.CallStatic("checkMissingOrder", Utility.Utility.CurrentActivity);
        }
    }
}

