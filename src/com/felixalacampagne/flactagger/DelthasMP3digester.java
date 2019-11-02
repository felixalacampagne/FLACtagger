package com.felixalacampagne.flactagger;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.felixalacampagne.utils.Utils;

import fr.delthas.javamp3.Sound;

public class DelthasMP3digester extends AbstractAudioDigester {

	@Override
	public String getAudioDigest(File audioFile) throws IOException {
		Sound sound = new Sound(new BufferedInputStream(new FileInputStream(audioFile)));
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		// This might consume too much memory...
		//int read = sound.decodeFullyInto(os);
		
		try
		{
			md = MessageDigest.getInstance("MD5");
			md.reset();
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		
		
		byte[] pcmbuffer = new byte[5*1024*2014];
		int bytesread = 0;
		while((bytesread = sound.read(pcmbuffer)) > 0)
		{
			md.update(pcmbuffer, 0, bytesread);
		}
		Utils.safeClose(sound);
		byte[] mdbytes = md.digest();
		calculatedMD5 = bytesToHex(mdbytes);		
		return calculatedMD5;
	}

}