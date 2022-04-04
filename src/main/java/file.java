import exception.fileException;
import jdk.jshell.spi.ExecutionControl;
import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@Setter
public class file {
    enum FILE_TYPE{
        FILE,
        FILES,
        DIRECTORY,
        DIRECTORYS
    }
    private fileException fileExce = new fileException();

    String[] _path;
    FILE_TYPE _pathType;
    List<File> dirs = new ArrayList<>();
    List<File> items = new ArrayList<>();
    List<File> nonExists = new ArrayList<>();
    public file(String ... path)  {
        this._path = path;
        List<File> files = new ArrayList<>();
        for (String s : this._path) {
            files.add(new File(s));
        }
        dirs = files.stream().filter(File::exists).filter(File::isDirectory).collect(toList());
        items =files.stream().filter(File::exists).filter(_file -> !_file.isDirectory()).collect(toList());

        for(File fs : dirs)
        {
            File[] test = fs.listFiles();
            items.addAll(Arrays.asList(test));

        }

        nonExists = files.stream().filter(file -> !file.exists()).collect(toList());

        for(File f: nonExists)
        {
            System.out.println(f.getAbsolutePath()+" 해당 파일은 존재 하지 않습니다.");
        }
    }
    public void giveMeResult(List<File> files) throws IOException {
        for(File f: files)
        {
            imageToRGD(f);
        }
    }
    private String[] inFiles(File f){
        if(!f.isDirectory())
        {
            fileExce.nonExistPath();
        }
        return f.list();
    }
    private void imageToRGD(File f) throws IOException {
        if(f.isDirectory()) {
            if (f.listFiles().length <= 0) {
                return;
            }
        }
        BufferedImage imageFile = ImageIO.read(f);
        File tempSave = new File(f.getParent()+"\\changed\\");
        if(!tempSave.exists())
        {
            tempSave.mkdir();
        }

        File outputFile = new File(tempSave.getAbsolutePath()+"\\"+f.getName());

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        int width = imageFile.getWidth();
        int height = imageFile.getHeight();
        List<Integer> removeColor= Arrays.asList(
                new Color(74, 186, 230).getRGB(),
                new Color(123, 202, 222).getRGB(),
                new Color(107, 113, 165).getRGB(),
                new Color(67, 185, 230).getRGB()

        );
        int tmps = new Color(222, 235, 239).getRGB();
        for(int widthX = 0;widthX<width;widthX++)
        {
            for(int heightY =0 ;heightY<height;heightY++)
            {
                int pointColor = imageFile.getRGB(widthX, heightY);
                Color cr = new Color(imageFile.getRGB(widthX, heightY));
//                if (removeColor.contains(imageFile.getRGB(widthX, heightY)))
//                {
//                    pointColor = Color.WHITE.getRGB();
//                    System.out.println(imageFile.getRGB(widthX, heightY));
//                }
                if(
                        ((225 <= cr.getRed() && cr.getRed() <= 260) &&
                        (225 <= cr.getGreen() && cr.getGreen() <= 260) &&
                        (225 <= cr.getBlue() && cr.getBlue() <= 260) )|| tmps == cr.getRGB()
                )
                {
                    pointColor = Color.WHITE.getRGB();
                }
                imageFile.setRGB(widthX,heightY,pointColor);
            }
        }
        ImageIO.write(imageFile,"png",outputFile);
    }
}
