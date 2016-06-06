/**
 * Класс для работы со звуком.
 * Требуется для оповещения о выполнении задачи.
 * @author Карасев
 * @version 1.0
 */
package sound;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;


public class Sound {
    SourceDataLine line;
    AudioFileFormat aff;
    AudioFormat af;
    DataLine.Info info;
    AudioInputStream ais;
    File f;
    byte[] b = new byte[2048]; // Буфер данных
    
    /**
     * Воспроизвести звуковой файл
     * @param f Звуковой файл
     */
    public  void playFile(File f) {
        this.f = f;
    }
    
    /**
     * Запускает новый поток для воспроизведения звукового файла.
     */
    public void start() {
        int num = 0;
        if (f != null && testFile(f)) {
            try {
                line = (SourceDataLine) AudioSystem.getLine(info);
                line.open(af);
                new Thread(new SoundT(line, ais, f)).start();
            } catch (Exception e) {
            } 
        }
    }

    /**
     * Проверяет файл на возможность его аудиовоспроизведения
     * @param f Проверяемый файл
     * @return Воспроизводимость файла
     */
    public boolean  testFile(File f) {
        try {
            ais = AudioSystem.getAudioInputStream(f);            
            af = ais.getFormat () ;
             info= new DataLine.Info(SourceDataLine.class, af);
            if (!AudioSystem.isLineSupported(info)) {
                return false;
            }
            return true;
        } catch (UnsupportedAudioFileException | IOException e) {
            return false;
        }
    }
}
/**
 * Поток для воспроизведения звука
 * @author Карасёв
 */
class SoundT implements Runnable{
    SourceDataLine line;
    AudioInputStream ais;
    byte[] b = new byte[2048];
    File f;
    
    public SoundT(SourceDataLine dataLine, AudioInputStream inputStream, File f){
        line=dataLine;
        ais=inputStream;
        this.f=f;
    }
    
    /**
     * Запуск звукового потока
     */
    @Override
    public void run() {
        int num;
        try {           
            line.start();
            while ((num = ais.read(b)) != -1) {
                line.write(b, 0, num);
            }
            line.drain();
            ais.close();
        } catch (Exception e) {
        } finally {
            line.stop();
            // Закрываем линию 
            line.close();
            f = null;
        }
    }
    
}