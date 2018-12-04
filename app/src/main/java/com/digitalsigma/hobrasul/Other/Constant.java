package com.digitalsigma.hobrasul.Other;

import java.util.ArrayList;

/**
 * Created by AhmedAbouElFadle on 11/29/2016.
 */
public class Constant {


    public static String album_id;
    public static String albumName;

    public static String registeration = "http://www.gms-sms.com:89/semsmshehabapp/UsersRegisteration.php";


    //sql links back end


    public static String ALL_ARTIST_KEY = "http://www.gms-sms.com:89/SemsmShehabApp/ListAllArtist.php";
    public static String ALL_SONG_KEY = "http://www.gms-sms.com:89/SemsmShehabApp/ListAllSongs.php";
    public static String SONGSWITHIDALBUM = "http://www.gms-sms.com:89/SemsmShehabApp/SongsWithAlbumId.php";
    //public static String SEARCHSONG="http://78.142.63.63/~tohamy/eltohamyapp/SongSearch.php";


    //change that
    public static String BASE_URL = "http://hubalrasul.digitalsigma.io";
    public static String SEARCHSONG = "http://www.gms-sms.com:89/SemsmShehabApp/SongSearch.php";
    public static String Search_key = "track_name";
    public static String all_tracks = BASE_URL + "/api/tracks";
    public static String ALL_GALLARY = BASE_URL + "/api/galleries";
    public static String all_albums = BASE_URL + "/api/albums";


    public static String all_tracks_with_album_id = "http://www.gms-sms.com:89/SemsmShehabApp/ListTracksWithAlbumId.php";

    //public static String ALL_GALLARY="http://78.142.63.63/~tohamy/eltohamyapp/Listgallary.php";

    // public static String ALL_GALLARY="http://www.gms-sms.com:89/eltohamyapp/Listgallary.php";
    public static String ALL_PARTIES = "http://www.gms-sms.com:89/SemsmShehabApp/allParties.php";
    public static String NEWS_URL = BASE_URL + "/api/articles";
    public static String TOKEN_REGISTER = BASE_URL + "/api/notification";


    public static String serachCalltoneVod;
    public static String serachCalltoneEt;
    public static String serachCalltoneOran;


    public static String img_url;


    public static String NOTIFY_PLAYER = "play";


    public static String album_name;


    public static String partyTitle;
    public static String partyTime;
    public static String partyPlace;


    public static String video_id = "";


    public static String ussd;
    public static int img_Id;
    public static String youtubeChannelId = "UC5MtCj2LVwX30SEF-dwXlww";
    public static String ALL_VIDEOS = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet%2CcontentDetails&maxResults=25&playlistId=PLO_vsaTqh2Zsk5wWlcoMCKK2frojOLCet&key=AIzaSyAV0ysZvmqBDggFnoeRHzw8IT4zV2Kxqos";
    public static int pointer = 0;
    public static String songName;
    public static String songUrl;

    public static int changedNewsIndex = 0;
    public static String newsNotificationDocmention;
    public static String newsNotificationDocmentionTilte;
    public static String newsNotificationDocmentionImgUrl;


    public static String newsImgUrl;


    public static ArrayList<String> newsTitle = new ArrayList<String>();
    public static ArrayList<String> newsDocmentaion = new ArrayList<String>();
    public static ArrayList<String> newsImgUrlContent = new ArrayList<String>();
    public static int newsPostion = 0;


    public static ArrayList<String> gallaryList = new ArrayList<String>();

    public static ArrayList<String> gallaryIdList = new ArrayList<String>();


    public static ArrayList<String> playListName = new ArrayList<String>();
    public static ArrayList<String> playListUrl = new ArrayList<String>();
    public static ArrayList<String> RingToneUrl = new ArrayList<String>();
    public static ArrayList<String> idList = new ArrayList<String>();

    public static String ringtoneUrl;


    public static int postion = 0;


    public static String networkName;


    public static ArrayList<String> vodCallToneList = new ArrayList<String>();
    public static ArrayList<String> orangCallToneList = new ArrayList<String>();

    public static ArrayList<String> EtisCallToneList = new ArrayList<String>();


    //Firebase app url
    public static final String FIREBASE_APP = "https://simplifiedcoding.firebaseio.com/";

    //Constant to store shared preferences
    public static final String SHARED_PREF = "mynotificationapp";

    //To store boolean in shared preferences for if the device is registered to not
    public static final String REGISTERED = "registered";

    //To store the firebase id in shared preferences
    public static final String UNIQUE_ID = "uniqueid";

    //register.php address in your server
    public static final String REGISTER_URL = "http://192.168.94.1/firebasepushnotification/register.php";


}
