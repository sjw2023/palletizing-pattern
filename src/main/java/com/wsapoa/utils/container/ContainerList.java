package com.wsapoa.utils.container;

import com.wsapoa.entity.Container;
import com.wsapoa.utils.pattern.AbstractPattern;
import com.wsapoa.utils.shape.Coordinate;
import com.wsapoa.utils.shape.ObjectAreaInfo;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ContainerList {
    private Container containerInfo;
    private List<AbstractPattern> patterns;
    private float containerVolumeLeft;
    private float containerLengthLeft;
    private float containerWidthLeft;
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
//        var x = pattern.isRotate()? pattern.getTotalPatternWidth() : pattern.getTotalPatternLength();
//        var y = pattern.isRotate()? pattern.getTotalPatternLength() : pattern.getTotalPatternWidth();
//        pattern.setCenter(new Coordinate(x / 2, y / 2, pattern.getTotalPatternHeight() / 2));
//        this.patterns.add(pattern);
//        this.containerVolumeLeft -= pattern.getPatternVolume();
//        this.containerLengthLeft -= x;
//    }

    public boolean isSpaceLeft(@NotNull AbstractPattern pattern) {
        return containerVolumeLeft > pattern.getPatternVolume();
    }

//    public boolean isPatternFitInContainer(@NotNull AbstractPattern pattern) {
//        return containerInfo.getHeight() > pattern.calcActualPatternHeight() && containerInfo.getWidth() > pattern.getTotalPatternWidth();
//    }

    public void addPattern(@NotNull AbstractPattern pattern, boolean rotate, int index) {
//        if (!isPatternFitInContainer(pattern)) return false;

        pattern.calcAreaEfficiency();
        adjustContainerLengthLeft(pattern, rotate, index);
        setPatternPosition(pattern, rotate, index);

        assert containerLengthLeft >= pattern.getTotalPatternLength() || containerLengthLeft >= pattern.getTotalPatternWidth() : "Container length is exceeded";
        assert containerVolumeLeft >= pattern.getPatternVolume() : "Container volume is exceeded";

        this.patterns.add(pattern);
        containerVolumeLeft -= pattern.getPatternVolume();
//        return true;
    }

    private void adjustContainerLengthLeft(AbstractPattern pattern, boolean rotate, int index) {
        if (patterns.isEmpty() || patterns.size() == 1) {
            if (index == 0) {
                containerLengthLeft -= !rotate ? pattern.getTotalPatternWidth() : pattern.getTotalPatternLength();
            }
        } else {
            if (index == 0) {
                containerLengthLeft -= !rotate ? pattern.getTotalPatternWidth() : pattern.getTotalPatternLength();
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
        float xStart = !rotate ? (float) pattern.getTotalPatternWidth() / 2 : (float) pattern.getTotalPatternLength() / 2;
        float increaseFactor = !rotate ? pattern.getTotalPatternLength() : pattern.getTotalPatternWidth();
        pattern.setCenter(new Coordinate((long) (xStart + index * increaseFactor), pattern.getTotalPatternWidth() / 2, pattern.getTotalPatternHeight() / 2));
        pattern.setRotate(rotate);
    }

    private void setSubsequentPatternPosition(@NotNull AbstractPattern pattern, boolean rotate, int index) {
        int numOfPatternsInWidth = (int) (containerInfo.getWidth() / pattern.getTotalPatternWidth());
        AbstractPattern lastPattern = patterns.getLast();
        if (index == 0) {
            lastPattern = patterns.get(patterns.size() - numOfPatternsInWidth);
            pattern.setCenter(new Coordinate(lastPattern.getCenter().getX(), lastPattern.getCenter().getY() + pattern.getTotalPatternLength(), lastPattern.getCenter().getZ()));
        } else {
            pattern.setCenter(new Coordinate(lastPattern.getCenter().getX() + index * pattern.getTotalPatternWidth(), lastPattern.getCenter().getY(), lastPattern.getCenter().getZ()));
        }
        pattern.setRotate(rotate);
    }

    public boolean isLengthLeft(@NotNull AbstractPattern pattern) {
        return !pattern.isRotate() ? containerLengthLeft >= pattern.getTotalPatternWidth() : containerLengthLeft >= pattern.getTotalPatternLength();
    }

    public float calcAreaEfficiency() {
        float patternArea = patterns.stream()
                .map(AbstractPattern::getActualPatternVolume)
                .reduce(0f, Float::sum);
        this.areaEfficiency = (patternArea / containerInfo.getContainerVolume()) * 100f;
        return this.areaEfficiency;
    }

    public long getNumberOfPatterns() {
        return patterns.size();
    }
}