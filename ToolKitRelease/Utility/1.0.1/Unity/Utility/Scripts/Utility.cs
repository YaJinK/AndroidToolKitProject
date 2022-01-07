using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace Com.Magata.Utility
{
    public class Utility
    {
        private static Utility instance = null;

        private AndroidJavaClass unityPlayer = null;

        private AndroidJavaObject currentActivity = null;

        private Utility()
        {
            unityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
            currentActivity = unityPlayer.GetStatic<AndroidJavaObject>("currentActivity");
        }

        public static Utility Instance
        {
            get
            {
                if (null == instance)
                {
                    instance = new Utility();
                }
                return instance;
            }
        }

        public AndroidJavaClass UnityPlayer
        {
            get
            {
                return unityPlayer;
            }
        }

        public AndroidJavaObject CurrentActivity
        {
            get
            {
                return currentActivity;
            }
        }

        public bool StartActivity(string url)
        {
            AndroidJavaClass utility = new AndroidJavaClass("com.magata.utility.Utility");
            bool result = utility.CallStatic<bool>("startActivity", currentActivity, url);
            utility.Dispose();

            return result;
        }

        public bool StartActivity(string url, string mimeType)
        {
            AndroidJavaClass utility = new AndroidJavaClass("com.magata.utility.Utility");
            bool result = utility.CallStatic<bool>("startActivity", currentActivity, url, mimeType);
            utility.Dispose();

            return result;
        }
    }
}

