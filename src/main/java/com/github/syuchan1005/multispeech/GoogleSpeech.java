package com.github.syuchan1005.multispeech;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by syuchan on 2016/11/17.
 */
public class GoogleSpeech implements Speech {
	private static HttpClient httpClient = HttpClientBuilder.create().build();
	private static AudioPlayer audioPlayer;

	public GoogleSpeech() throws LineUnavailableException {
		this(new InlineAudioPlayer());
	}

	public GoogleSpeech(AudioPlayer audioPlayer) {
		this.audioPlayer = audioPlayer;
	}

	private byte[] sendTalk(String msg) throws IOException, UnsupportedAudioFileException {
		HttpGet httpGet = new HttpGet("https://translate.google.com/" +
				"translate_tts?ie=UTF-8&tl=ja&client=z&q=" + msg);
		HttpResponse response = httpClient.execute(httpGet);
		AudioInputStream audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(response.getEntity().getContent()));
		AudioInputStream audioOut = AudioSystem.getAudioInputStream(InlineAudioPlayer.linearPCM, audioIn);
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		while (true) {
			int len = audioOut.read(buffer);
			if (len < 0) break;
			bout.write(buffer, 0, len);
		}
		return bout.toByteArray();
	}

	@Override
	public void talk(String msg) throws IOException, UnsupportedAudioFileException {
		audioPlayer.addQueue(sendTalk(msg));
	}
}
