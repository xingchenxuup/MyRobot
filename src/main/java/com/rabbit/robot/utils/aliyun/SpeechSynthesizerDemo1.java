package com.rabbit.robot.utils.aliyun;

/**
 * @Author: 邢晨旭
 * @Date: 2021/4/12 15:35
 * @Description:
 */

import com.alibaba.nls.client.AccessToken;
import com.alibaba.nls.client.protocol.NlsClient;
import com.alibaba.nls.client.protocol.OutputFormatEnum;
import com.alibaba.nls.client.protocol.SampleRateEnum;
import com.alibaba.nls.client.protocol.tts.SpeechSynthesizer;
import com.alibaba.nls.client.protocol.tts.SpeechSynthesizerListener;
import com.alibaba.nls.client.protocol.tts.SpeechSynthesizerResponse;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.Voice;
import net.mamoe.mirai.utils.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 此示例演示了：
 *      语音合成API调用。
 *      动态获取token。
 *      流式合成TTS。
 *      首包延迟计算。
 */
public class SpeechSynthesizerDemo1 {
    private static final Logger logger = LoggerFactory.getLogger(SpeechSynthesizerDemo1.class);
    private static long startTime;
    private String appKey;
    NlsClient client;

    public SpeechSynthesizerDemo1(String appKey, String accessKeyId, String accessKeySecret, String url) {
        this.appKey = appKey;
        AccessToken accessToken = new AccessToken(accessKeyId, accessKeySecret);
        try {
            accessToken.apply();
            System.out.println("get token: " + accessToken.getToken() + ", expire time: " + accessToken.getExpireTime());
            if(url.isEmpty()) {
                client = new NlsClient(accessToken.getToken());
            }else {
                client = new NlsClient(url, accessToken.getToken());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static SpeechSynthesizerListener getSynthesizerListener(Group contact) {
        SpeechSynthesizerListener listener = null;
        try {
            listener = new SpeechSynthesizerListener() {
                File wav=new File("tts_test.mp3");
                File amr=new File("tts_test.amr");
                FileOutputStream fout = new FileOutputStream(wav);
                private boolean firstRecvBinary = true;
                //语音合成结束
                @Override
                public void onComplete(SpeechSynthesizerResponse response) {
                    //调用onComplete时表示所有TTS数据已接收完成，因此为整个合成数据的延迟。该延迟可能较大，不一定满足实时场景。
//                    System.out.println("name: " + response.getName() +
//                            ", status: " + response.getStatus()+
//                            ", output file :"+f.getAbsolutePath()
//                    );
                    //AudioUtils.convert(wav,amr,"amr");
                    AudioAttributes audio = new AudioAttributes();
                    audio.setCodec("libamr_nb");//编码器

                    audio.setBitRate(12200);//比特率
                    audio.setChannels(1);//声道；1单声道，2立体声
                    audio.setSamplingRate(8000);//采样率（重要！！！）
                    audio.setVolume(256);
                    EncodingAttributes attrs = new EncodingAttributes();
                    attrs.setFormat("amr");//格式
                    attrs.setAudioAttributes(audio);//音频设置
                    Encoder encoder = new Encoder();
                    try {
                        encoder.encode(wav, amr, attrs);
                    } catch (EncoderException e) {
                        e.printStackTrace();
                    }
                    Voice voice = ExternalResource.uploadAsVoice(ExternalResource.create(amr, "amr"), contact);
                    contact.sendMessage(voice);
                }
                //语音合成的语音二进制数据
                @Override
                public void onMessage(ByteBuffer message) {
                    try {
                        if(firstRecvBinary) {
                            //计算首包语音流的延迟，收到第一包语音流时，即可以进行语音播放，以提升响应速度（特别是实时交互场景下）。
                            firstRecvBinary = false;
                            long now = System.currentTimeMillis();
                            logger.info("tts first latency : " + (now - SpeechSynthesizerDemo1.startTime) + " ms");
                        }
                        byte [] bytesArray = new byte[message.remaining()];
                        message.get(bytesArray, 0, bytesArray.length);
                        fout.write(bytesArray);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFail(SpeechSynthesizerResponse response){
                    //task_id是调用方和服务端通信的唯一标识，当遇到问题时需要提供task_id以便排查。
//                    System.out.println(
//                            "task_id: " + response.getTaskId() +
//                                    //状态码 20000000 表示识别成功
//                                    ", status: " + response.getStatus() +
//                                    //错误信息
//                                    ", status_text: " + response.getStatusText());
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listener;
    }
    public void process(Group contact,String msg) {
        SpeechSynthesizer synthesizer = null;
        try {
            //创建实例，建立连接。
            synthesizer = new SpeechSynthesizer(client, getSynthesizerListener(contact));
            synthesizer.setAppKey(appKey);
            //设置返回音频的编码格式
            synthesizer.setFormat(OutputFormatEnum.MP3);
            //设置返回音频的采样率
            synthesizer.setSampleRate(SampleRateEnum.SAMPLE_RATE_16K);
            //发音人
            synthesizer.setVoice("aibao");
            //语调，范围是-500~500，可选，默认是0。
            synthesizer.setPitchRate(100);
            //语速，范围是-500~500，默认是0。
            synthesizer.setSpeechRate(100);
            //设置用于语音合成的文本
            synthesizer.setText(msg);
            // 是否开启字幕功能（返回相应文本的时间戳），默认不开启，需要注意并非所有发音人都支持该参数。
            synthesizer.addCustomedParam("enable_subtitle", false);
            //此方法将以上参数设置序列化为JSON格式发送给服务端，并等待服务端确认。
            long start = System.currentTimeMillis();
            synthesizer.start();
            logger.info("tts start latency " + (System.currentTimeMillis() - start) + " ms");
            SpeechSynthesizerDemo1.startTime = System.currentTimeMillis();
            //等待语音合成结束
            synthesizer.waitForComplete();
            logger.info("tts stop latency " + (System.currentTimeMillis() - start) + " ms");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭连接
            if (null != synthesizer) {
                synthesizer.close();
            }
        }
    }
    public void shutdown() {
        client.shutdown();
    }
    public static void sendVoice(Group contact,String msg) {
        //TODO
        String appKey = "";
        String id = "";
        String secret = "";
        String url = ""; //默认值：wss://nls-gateway.cn-shanghai.aliyuncs.com/ws/v1
        SpeechSynthesizerDemo1 demo = new SpeechSynthesizerDemo1(appKey, id, secret, url);
        demo.process(contact,msg);
        demo.shutdown();
    }
}