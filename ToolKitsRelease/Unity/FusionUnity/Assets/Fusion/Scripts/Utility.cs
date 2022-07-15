using UnityEngine;

namespace Com.Magata.Utility
{
    public class Utility
    {
        private static AndroidJavaObject currentActivity = null;

        public static AndroidJavaObject CurrentActivity
        {
            get
            {
                if (null == currentActivity)
                {
                    AndroidJavaClass unityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
                    currentActivity = unityPlayer.GetStatic<AndroidJavaObject>("currentActivity");
                }
                return currentActivity;
            }
        }

        public static void StartActivity(string url)
        {
            AndroidJavaClass utility = new AndroidJavaClass("com.magata.utility.Utility");
            utility.CallStatic("startActivity", CurrentActivity, url);
            utility.Dispose();
        }

        public static void StartActivity(string action, string url)
        {
            AndroidJavaClass utility = new AndroidJavaClass("com.magata.utility.Utility");
            utility.CallStatic<bool>("startActivity", CurrentActivity, action, url);
            utility.Dispose();
        }

        public static void StartActivity(string action, string url, string mimeType)
        {
            AndroidJavaClass utility = new AndroidJavaClass("com.magata.utility.Utility");
            utility.CallStatic<bool>("startActivity", CurrentActivity, action, url, mimeType);
            utility.Dispose();
        }

        public static string GetIntentAction(string actionName)
        {
            AndroidJavaClass intent = new AndroidJavaClass("android.content.Intent");
            string name = "";
            name = intent.GetStatic<string>(actionName);
            if ("".Equals(name))
                name = intent.GetStatic<string>("ACTION_VIEW");

            return name;
        }

        public static bool CheckClientInstalled(string packageName)
        {
            AndroidJavaClass utility = new AndroidJavaClass("com.magata.utility.Utility");
            bool result = utility.CallStatic<bool>("checkClientInstalled", CurrentActivity, packageName);
            utility.Dispose();

            return result;
        }


        public static void JoinQQGroup(string key)
        {
            AndroidJavaClass utility = new AndroidJavaClass("com.magata.utility.Utility");
            utility.CallStatic("joinQQGroup", CurrentActivity, key);
            utility.Dispose();
        }


        public static void ChatQQ(string qq)
        {
            AndroidJavaClass utility = new AndroidJavaClass("com.magata.utility.Utility");
            utility.CallStatic("chatQQ", CurrentActivity, qq);
            utility.Dispose();
        }

        public static string GetAndroidId()
        {
            AndroidJavaClass utility = new AndroidJavaClass("com.magata.utility.Utility");
            string result = utility.CallStatic<string>("getAndroidId", CurrentActivity);
            utility.Dispose();

            return result;
        }

        public static string GetUUID()
        {
            AndroidJavaClass utility = new AndroidJavaClass("com.magata.utility.Utility");
            string result = utility.CallStatic<string>("getUUID");
            utility.Dispose();

            return result;
        }

        public static int GetNotchHeight()
        {
            AndroidJavaClass utility = new AndroidJavaClass("com.magata.utility.Utility");
            int result = utility.CallStatic<int>("getNotchHeight", CurrentActivity);
            utility.Dispose();

            return result;
        }

        public static bool CheckIsNotch()
        {
            AndroidJavaClass utility = new AndroidJavaClass("com.magata.utility.Utility");
            bool result = utility.CallStatic<bool>("checkIsNotch", CurrentActivity);
            utility.Dispose();

            return result;
        }
    }
}

