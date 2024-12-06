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

//TODO : Consider use of width and length value to make consistency through the whole class.

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
    public ContainerList( @NotNull AbstractPattern abstractPattern){

        this.containerInfo = abstractPattern.getContainerInfo();
        this.patterns = new ArrayList<>();
        this.containerVolumeLeft = containerInfo.getContainerVolume();
        this.containerLengthLeft = containerInfo.getLength();
        this.containerAreaInfo = new ObjectAreaInfo(containerInfo, new Coordinate(containerInfo.getLength() / 2, containerInfo.getWidth() / 2, containerInfo.getHeight() / 2), false);
    }

    public boolean isSpaceLeft(@NotNull AbstractPattern pattern) {
        return containerVolumeLeft > pattern.calcActualPatternVolume();
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
//        if (patterns.isEmpty() || patterns.size() == 1) {
//            containerLengthLeft -= rotate ? pattern.getPatternWidth() : pattern.getPatternLength();
//        } else {
            if (index == 0) {
                containerLengthLeft -= rotate ? pattern.getPatternWidth() : pattern.getPatternLength();
            }
//        }
    }

    private AbstractPattern setPatternPosition( AbstractPattern pattern, boolean rotate, int index ) {
        if (patterns.isEmpty() ) {
            setFirstPatternPosition( pattern, rotate );
        } else {
            setSubsequentPatternPosition( pattern, rotate, index );
        }
        return pattern;
    }

    /**
     * Set the first pattern position in the container
     * X means container's width direction
     * Y means container's length direction
     * @param pattern
     * @param rotate
     */
    private void setFirstPatternPosition( @NotNull AbstractPattern pattern, boolean rotate ) {
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
        //TODO : This part is kinda buggy, find better way to keep consistency on the pattern's width and length
        int numOfPatternsInWidth =
                rotate ?
                        (int) (containerInfo.getWidth() / pattern.getPatternLength()) :
                        (int) (containerInfo.getWidth() / pattern.getPatternWidth()) ;
        long increaseY = rotate ?  pattern.getPatternWidth() : pattern.getPatternLength() ;
        long increaseX = rotate ?  pattern.getPatternLength() : pattern.getPatternWidth();

        if (index == 0) {
            AbstractPattern lastPattern = patterns.get(patterns.size() - numOfPatternsInWidth);
            pattern.setCenter(new Coordinate( lastPattern.getCenter().getX() , lastPattern.getCenter().getY() + increaseY, lastPattern.getCenter().getZ()));
        } else {
            AbstractPattern lastPattern = patterns.get(patterns.size() - 1);
            pattern.setCenter(new Coordinate( lastPattern.getCenter().getX() + increaseX, lastPattern.getCenter().getY() , lastPattern.getCenter().getZ()));
        }
        pattern.setRotate(rotate);
    }

    public boolean isLengthLeft(@NotNull AbstractPattern pattern, boolean rotate) {
        return rotate ? containerLengthLeft >= pattern.getPatternWidth() : containerLengthLeft >= pattern.getPatternLength();
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