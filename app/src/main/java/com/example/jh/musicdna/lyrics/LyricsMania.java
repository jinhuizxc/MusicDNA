package com.example.jh.musicdna.lyrics;

import com.example.jh.musicdna.annotations.Reflection;
import com.example.jh.musicdna.utils.Net;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

import java.io.IOException;
import java.text.Normalizer;
import java.util.Locale;

import static com.example.jh.musicdna.lyrics.Lyrics.ERROR;
import static com.example.jh.musicdna.lyrics.Lyrics.NEGATIVE_RESULT;
import static com.example.jh.musicdna.lyrics.Lyrics.POSITIVE_RESULT;

/**
 * Created by jinhui on 2018/2/19.
 * Email:1004260403@qq.com
 */

public class LyricsMania {

    @Reflection
    public static final String domain = "www.lyricsmania.com";
    private static final String baseURL = "http://www.lyricsmania.com/%s_lyrics_%s.html";

    @Reflection
    public static Lyrics fromMetaData(String artist, String song) {
        String htmlArtist = Normalizer.normalize(artist.replaceAll("[\\s-]", "_"), Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "").replaceAll("[^A-Za-z0-9_]", "");
        String htmlSong = Normalizer.normalize(song.replaceAll("[\\s-]", "_"), Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "").replaceAll("[^A-Za-z0-9_]", "");

        if (artist.startsWith("The "))
            htmlArtist = htmlArtist.substring(4) + "_the";

        String urlString = String.format(
                baseURL,
                htmlSong.toLowerCase(Locale.getDefault()),
                htmlArtist.toLowerCase(Locale.getDefault()));
        return fromURL(urlString, artist, song);
    }

    @Reflection
    public static Lyrics fromURL(String url, String artist, String title) {
        String text;
        try {
            Document document = Jsoup.connect(url).userAgent(Net.USER_AGENT).get();
            Element lyricsBody = document.getElementsByClass("lyrics-body").get(0);
            // lyricsBody.select("div").last().remove();
            text = Jsoup.clean(lyricsBody.html(), "", Whitelist.basic().addTags("div"));
            text = text.substring(text.indexOf("</strong>")+10, text.lastIndexOf("</div>"));

            String[] keywords =
                    document.getElementsByTag("meta").attr("name", "keywords").get(0).attr("content").split(",");

            if (artist == null)
                artist = document.getElementsByClass("lyrics-nav-menu").get(0)
                        .getElementsByTag("a").get(0).text();
            if (title == null)
                title = keywords[0];
        } catch (HttpStatusException | IndexOutOfBoundsException e) {
            return new Lyrics(Lyrics.NO_RESULT);
        } catch (IOException e) {
            return new Lyrics(ERROR);
        }
        if (text.startsWith("Instrumental"))
            return new Lyrics(NEGATIVE_RESULT);
        Lyrics lyrics = new Lyrics(POSITIVE_RESULT);
        lyrics.setArtist(artist);
        lyrics.setTitle(title);
        lyrics.setURL(url);
        lyrics.setSource(domain);
        lyrics.setText(text.trim());
        return lyrics;
    }

}
