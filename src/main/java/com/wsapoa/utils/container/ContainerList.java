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
        pattern.calcAreaEfficiency();
        assert containerLengthLeft >= pattern.getActualPatternLength() || containerLengthLeft >= pattern.getActualPatternWidth() : "Container length is exceeded";
        assert containerVolumeLeft >= pattern.calcTotalPatternVolume() : "Container volume is exceeded";
        this.patterns.add(setPatternPosition(pattern, rotate, index));
        adjustContainerLengthLeft(pattern, rotate, index);
        containerVolumeLeft -= pattern.getPatterVolume();
        return true;
    }

    private void adjustContainerLengthLeft(AbstractPattern pattern, boolean rotate, int index) {
        if (patterns.isEmpty() || patterns.size() == 1) {
            containerLengthLeft -= rotate ? pattern.getPatternWidth() : pattern.getPatternLength();
        } else {
            if (index == 0) {
                containerLengthLeft -= rotate ? pattern.getPatternWidth() : pattern.getPatternLength();
            }
        }
    }

    private AbstractPattern setPatternPosition(AbstractPattern pattern, boolean rotate, int index) {
        if (patterns.isEmpty() ) {
            setFirstPatternPosition(pattern, rotate, index);
        } else {
            setSubsequentPatternPosition(pattern, rotate, index);
        }
        return pattern;
    }

    private void setFirstPatternPosition(@NotNull AbstractPattern pattern, boolean rotate, int index) {
        float xStart =
                rotate ?
                        (float) pattern.getPatternLength() / 2 :
                        (float) pattern.getPatternWidth() / 2;
        float yStart =
                rotate ?
                        (float) pattern.getPatternWidth() / 2 :
                        (float) pattern.getPatternLength() / 2;
        pattern.setCenter(new Coordinate((long) xStart, (long) yStart, pattern.getTotalPatternHeight() / 2));
        pattern.setRotate(rotate);
    }

    private void setSubsequentPatternPosition(@NotNull AbstractPattern pattern, boolean rotate, int index) {
        int numOfPatternsInWidth =
                rotate ?
                        (int) (containerInfo.getWidth() / pattern.getPatternLength()) :
                        (int) (containerInfo.getWidth() / pattern.getPatternWidth());
        long increaseY = rotate ? pattern.getPatternLength() : pattern.getPatternWidth();
        long increaseX = rotate ? pattern.getPatternWidth() : pattern.getPatternLength();
        if (index == 0) {
            AbstractPattern lastPattern = patterns.get(patterns.size() - numOfPatternsInWidth);
            pattern.setCenter(new Coordinate(lastPattern.getCenter().getX() + increaseX, lastPattern.getCenter().getY() , lastPattern.getCenter().getZ()));
        } else {
            AbstractPattern lastPattern = patterns.get(patterns.size() - 1);
            pattern.setCenter(new Coordinate( lastPattern.getCenter().getX() ,lastPattern.getCenter().getY()+ increaseY, lastPattern.getCenter().getZ()));
        }
        pattern.setRotate(rotate);
    }

    public boolean isLengthLeft(@NotNull AbstractPattern pattern, boolean rotate) {
        return rotate ? containerLengthLeft >= pattern.getPatternLength() : containerLengthLeft >= pattern.getPatternWidth();
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