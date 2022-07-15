using UnityEngine;

namespace Com.Magata.Config
{
    public class ServerConfig
    {
        public static void InitAccountUrl(string accountUrl)
        {
            AndroidJavaClass serverConfig = new AndroidJavaClass("com.magata.config.ServerConfig");
            serverConfig.SetStatic("accountUrl", accountUrl);
            serverConfig.Dispose();
        }

        public static void InitPayUrl(string payUrl)
        {
            AndroidJavaClass serverConfig = new AndroidJavaClass("com.magata.config.ServerConfig");
            serverConfig.SetStatic("payUrl", payUrl);
            serverConfig.Dispose();
        }
    }
}

