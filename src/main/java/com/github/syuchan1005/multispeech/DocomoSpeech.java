package com.github.syuchan1005.multispeech;

import jp.ne.docomo.smt.dev.aitalk.AiTalkTextToSpeech;
import jp.ne.docomo.smt.dev.aitalk.data.AiTalkSsml;
import jp.ne.docomo.smt.dev.common.exception.RestApiException;
import jp.ne.docomo.smt.dev.common.http.AuthApiKey;

import javax.sound.sampled.LineUnavailableException;

/**
 * Created by syuchan on 2016/11/17.
 */
public class DocomoSpeech implements Speech {
	private static AiTalkTextToSpeech aiTalkTextToSpeech = new AiTalkTextToSpeech();
	private static AudioPlayer audioPlayer;

	public DocomoSpeech(String apiKey) throws LineUnavailableException {
		AuthApiKey.initializeAuth(apiKey);
		audioPlayer = new AudioPlayer();
	}

	@Override
	public void talk(String msg) throws Exception {
		audioPlayer.addQueue(sendTalk("maki", msg));
	}

	private byte[] sendTalk(String speaker, String text) throws RestApiException {
		AiTalkSsml aiTalkSsml = new AiTalkSsml();
		aiTalkSsml.startVoice(speaker);
		aiTalkSsml.addText(text.replace('<', ' ').replace('>', ' '));
		aiTalkSsml.endVoice();
		return aiTalkTextToSpeech.requestAiTalkSsmlToSound(aiTalkSsml.makeSsml());
	}
}
