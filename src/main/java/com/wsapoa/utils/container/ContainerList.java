package com.wsapoa.utils.container;

import com.wsapoa.entity.Container;
import com.wsapoa.utils.pattern.AbstractPattern;
import com.wsapoa.utils.shape.Coordinate;
import com.wsapoa.utils.shape.ObjectAreaInfo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ContainerList {
    private Container containerInfo;
    private List<AbstractPattern> patterns;
    private float containerVolumeLeft;
    private float containerLengthLeft;
    private float areaEfficiency;
    private ObjectAreaInfo containerAreaInfo;

    public ContainerList(@NotNull Container containerInfo) {
        this.containerInfo = containerInfo;
        this.patterns = new ArrayList<>();
        this.containerVolumeLeft = containerInfo.getContainerVolume();
        this.containerLengthLeft = containerInfo.getLength();
        this.containerAreaInfo = new ObjectAreaInfo(containerInfo, new Coordinate(containerInfo.getLength() / 2, containerInfo.getWidth() / 2, containerInfo.getHeight() / 2), false);
    }

//    public ContainerList(@NotNull AbstractPattern pattern, Container containerInfo) {
//        this(containerInfo);
//        if (pattern != null) {
//            initializePattern(pattern);
//        }
//    }

//    private void initializePattern(@NotNull AbstractPattern pattern) {
//        var x = pattern.isRotate()? pattern.getActualPatternWidth() : pattern.getActualPatternLength();
//        var y = pattern.isRotate()? pattern.getActualPatternLength() : pattern.getActualPatternWidth();
//        pattern.setCenter(new Coordinate(x / 2, y / 2, pattern.getTotalPatternHeight() / 2));
//        this.patterns.add(pattern);
//        this.containerVolumeLeft -= pattern.getPatternVolume();
//        this.containerLengthLeft -= x;
//    }

    public boolean isSpaceLeft(@NotNull AbstractPattern pattern) {
        return containerVolumeLeft > pattern.calcActualPatternVolume();
    }

    public boolean isPatternFitInContainer(@NotNull AbstractPattern pattern) {
        return containerInfo.getWidth() > pattern.getActualPatternWidth();
    }
    public boolean addPattern(@NotNull AbstractPattern pattern, boolean rotate, int index) {
//        if (!isPatternFitInContainer(pattern)) return false;
        pattern.calcAreaEfficiency();
        assert containerLengthLeft >= pattern.getActualPatternLength() || containerLengthLeft >= pattern.getActualPatternWidth() : "Container length is exceeded";
        assert containerVolumeLeft >= pattern.calcTotalPatternVolume() : "Container volume is exceeded";
        setPatternPosition(pattern, rotate, index);
        this.patterns.add(pattern);
        adjustContainerLengthLeft(pattern, rotate, index);
        containerVolumeLeft -= pattern.getPatterVolume();
        return true;
    }

    private void adjustContainerLengthLeft(AbstractPattern pattern, boolean rotate, int index) {
        if (patterns.isEmpty() || patterns.size() == 1) {
            if (index == 0) {
                containerLengthLeft -= rotate ? pattern.getPatternWidth() : pattern.getPatternLength();
//                containerLengthLeft -= pattern.getActualPatternLength();
            }
        } else {
            if (index == 0) {
                containerLengthLeft -= rotate ? pattern.getPatternWidth() : pattern.getPatternLength();
//                containerLengthLeft -= pattern.getActualPatternLength();
            }
        }
    }

    private void setPatternPosition(AbstractPattern pattern, boolean rotate, int index) {
        if (patterns.isEmpty() || patterns.size() == 1) {
            setFirstPatternPosition(pattern, rotate, index);
        } else {
            setSubsequentPatternPosition(pattern, rotate, index);
        }
    }

    private void setFirstPatternPosition(@NotNull AbstractPattern pattern, boolean rotate, int index) {
        float xStart = rotate ? (float) pattern.getPatternWidth()/ 2 : (float) pattern.getPatternLength() / 2;
        float increaseFactor = rotate ? pattern.getPatternLength() : pattern.getPatternWidth();
        pattern.setCenter(new Coordinate((long) (xStart + index * increaseFactor), pattern.getPatternWidth() / 2, pattern.getTotalPatternHeight() / 2));
        pattern.setRotate(rotate);
    }

    private void setSubsequentPatternPosition(@NotNull AbstractPattern pattern, boolean rotate, int index) {
        int numOfPatternsInWidth = rotate ? (int) (containerInfo.getWidth() / pattern.getPatternLength()) :(int) (containerInfo.getWidth() / pattern.getPatternWidth());
        AbstractPattern lastPattern = patterns.get(patterns.size() - 1);
        if (index == 0) {
            lastPattern = patterns.get(patterns.size() - numOfPatternsInWidth);
            pattern.setCenter(new Coordinate(lastPattern.getCenter().getX(), lastPattern.getCenter().getY() + pattern.getPatternLength(), lastPattern.getCenter().getZ()));
        } else {
            pattern.setCenter(new Coordinate(lastPattern.getCenter().getX() + index * pattern.getPatternWidth(), lastPattern.getCenter().getY(), lastPattern.getCenter().getZ()));
        }
        pattern.setRotate(rotate);
    }

    public boolean isLengthLeft(@NotNull AbstractPattern pattern) {
        return pattern.isRotate() ? containerLengthLeft >= pattern.getPatternWidth() : containerLengthLeft >= pattern.getPatternLength();
    }

    public float calcAreaEfficiency() {
        float patternArea = patterns.stream()
                .map(AbstractPattern::getPatterVolume)
                .reduce(0f, Float::sum);
        this.areaEfficiency = (patternArea / containerInfo.getContainerVolume()) * 100f;
        return this.areaEfficiency;
    }

    public long getNumberOfPatterns() {
        return patterns.size();
    }
}