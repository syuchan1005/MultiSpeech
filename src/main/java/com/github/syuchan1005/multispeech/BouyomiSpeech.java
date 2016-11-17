package com.github.syuchan1005.multispeech;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by syuchan on 2016/11/17.
 */
public class BouyomiSpeech implements Speech {
	private String host;
	private int port;

	public BouyomiSpeech()  {
		this("localhost", 50001);
	}

	public BouyomiSpeech(String host, int port)  {
		this.host = host;
		this.port = port;
	}

	public void clear() throws IOException {
		sendCommand(64);
	}

	public void pause() throws IOException {
		sendCommand(16);
	}

	public void resume() throws IOException {
		sendCommand(32);
	}

	public void skip() throws IOException {
		sendCommand(48);
	}

	@Override
	public void talk(String message) throws Exception {
		sendTalk((short) -1, (short) -1, (short) -1, (short) 0, message);
	}

	private void sendCommand(int command) throws IOException {
		byte data[] = new byte[2];
		data[0] = (byte) ((command) & 255);
		data[1] = (byte) ((command >>> 8) & 0xFF);
		send(data);
	}

	private void sendTalk(short volume, short speed, short tone, short voice, String message) throws IOException {
		byte messageData[] = message.getBytes("UTF-8");
		int length = messageData.length;
		byte data[] = new byte[15 + length];
		data[0] = (byte) 1;
		data[1] = (byte) 0;
		data[2] = (byte) ((speed) & 255);
		data[3] = (byte) ((speed >>> 8) & 255);
		data[4] = (byte) ((tone) & 255);
		data[5] = (byte) ((tone >>> 8) & 255);
		data[6] = (byte) ((volume) & 255);
		data[7] = (byte) ((volume >>> 8) & 255);
		data[8] = (byte) ((voice) & 255);
		data[9] = (byte) ((voice >>> 8) & 255);
		data[10] = (byte) 0;
		data[11] = (byte) ((length) & 255);
		data[12] = (byte) ((length >>> 8) & 255);
		data[13] = (byte) ((length >>> 16) & 255);
		data[14] = (byte) ((length >>> 24) & 255);
		System.arraycopy(messageData, 0, data, 15, length);
		send(data);
	}

	private void send(byte[] data) throws IOException {
		new DataOutputStream(new Socket(host, port).getOutputStream()).write(data);
	}
}
