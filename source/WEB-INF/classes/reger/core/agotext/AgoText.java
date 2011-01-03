package reger.core.agotext;

import org.apache.log4j.Logger;

import java.util.*;

import reger.core.agotext.units.Second;
import reger.core.agotext.units.Year;
import reger.core.agotext.units.Month;
import reger.core.TimeUtils;

/**
 * User: Joe Reger Jr
 * Date: Jun 19, 2009
 * Time: 6:59:02 AM
 */
public class AgoText {

    private Date reference;
    private List<TimeUnit> units;

    {
        units = new ArrayList<TimeUnit>();
        units.add(TimeUnit.SECOND);
        units.add(TimeUnit.MINUTE);
        units.add(TimeUnit.HOUR);
        units.add(TimeUnit.DAY);
        //units.add(TimeUnit.WEEK);
        units.add(TimeUnit.MONTH);
        units.add(TimeUnit.YEAR);
    }

    /**
     * Default constructor
     */
    public AgoText()
    {}

    /**
     * Constructor accepting a Date timestamp to represent the point of
     * reference for comparison. This may be changed by the user, after
     * construction.
     * <p>
     * See {@code PrettyTime.setReference(Date timestamp)}.
     *
     * @param reference
     */
    public AgoText(final Date reference){
        setReference(reference);
    }




    /**
     * Calculate the approximate duration between the referenceDate and date
     *
     * @param then
     * @return
     */
    public Duration approximateDuration(final Date then)
    {
        Date ref = reference;
        if (ref == null){
            ref = TimeUtils.nowInGmtCalendar().getTime();
        }

        long difference = then.getTime() - ref.getTime();
        return calculateDuration(difference);
    }


    private Duration calculateDuration(final long difference)
    {

        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("calculateDuration called");
        long absoluteDifference = Math.abs(difference);

        Duration result = new Duration();

        for (int i = 0; i < units.size(); i++){
            TimeUnit unit = units.get(i);
            long millisPerUnit = Math.abs(unit.getMillisPerUnit());
            long quantityToNextLargerUnit= Math.abs(unit.getMaxQuantity());


            boolean isLastUnit = (i == units.size() - 1);
            logger.debug("unit="+unit.getName()+" isLastUnit="+isLastUnit);

            //Basically this is the quantityToNextLargerUnit to the next one up
            if (quantityToNextLargerUnit == 0 && !isLastUnit) {
                quantityToNextLargerUnit= units.get(i + 1).getMillisPerUnit() / unit.getMillisPerUnit();
                logger.debug("unit.getName()="+unit.getName()+" units.get(i+1).getName()="+units.get(i+1).getName()+" quantityToNextLargerUnit="+ quantityToNextLargerUnit);
            }


            //If the next larger unit up is too big or it's the last unit
            logger.debug("absoluteDifference / millisPerUnit="+absoluteDifference / millisPerUnit);
            logger.debug("millisPerUnit*quantityToNextLargerUnit="+millisPerUnit*quantityToNextLargerUnit+" absoluteDifference="+absoluteDifference);
            if (millisPerUnit*quantityToNextLargerUnit > absoluteDifference || isLastUnit){
                logger.debug("made it into loop jhjgfhgfjh");
                if(millisPerUnit > absoluteDifference){
                    logger.debug("millisPerUnit > absoluteDifference rounding up");
                    // we are rounding up: get 1 or -1 for past or future
                    //result.setQuantity(difference / absoluteDifference);
                } else {
                    result.setUnit(unit);
                    result.setQuantity(difference / millisPerUnit);
                    result.setDelta(difference - result.getQuantity() * millisPerUnit);
                }
                result.setUnit(unit);
                result.setQuantity(difference / millisPerUnit);
                result.setDelta(difference - result.getQuantity() * millisPerUnit);
                break;
            }
        }
        return result;
    }

    /**
     * Calculate to the precision of the smallest provided {@link TimeUnit}, the
     * exact duration represented by the difference between the reference
     * timestamp, and {@code then}
     * <p>
     * <b>Note</b>: Precision may be lost if no supplied {@link TimeUnit} is
     * granular enough to represent one millisecond
     *
     * @param then
     *            The date to be compared against the reference timestamp, or
     *            <i>now</i> if no reference timestamp was provided
     * @return A sorted {@link List} of {@link Duration} objects, from largest
     *         to smallest. Each element in the list represents the approximate
     *         duration (number of times) that {@link TimeUnit} to fit into the
     *         previous element's delta. The first element is the largest
     *         {@link TimeUnit} to fit within the total difference between
     *         compared dates.
     */
    public List<Duration> calculatePreciseDuration(final Date then){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("calculatePreciseDuration() called");
        int numberUnitsToShow = 2;
        int numberUnitsShown = 0;
        if (reference == null){ reference = TimeUtils.nowInGmtCalendar().getTime(); }
        List<Duration> result = new ArrayList<Duration>();
        long difference = then.getTime() - reference.getTime();
        Duration duration = calculateDuration(difference);
        result.add(duration);
        numberUnitsShown = numberUnitsShown +  1;
        logger.debug("duration.getUnit().getName()="+duration.getUnit().getName()+" duration.getDelta()="+duration.getDelta()+" duration.getQuantity()="+duration.getQuantity());
        logger.debug("added duration (out of while loop)");
        //Only consider a second/third term if this is a year or month
        if ((duration.getUnit() instanceof Year) || (duration.getUnit() instanceof Month)){
            while ((Math.abs(duration.getDelta()) > 0) && numberUnitsShown<numberUnitsToShow){
                duration = calculateDuration(duration.getDelta());
                logger.debug("duration.getUnit().getName()="+duration.getUnit().getName()+" duration.getDelta()="+duration.getDelta()+" duration.getQuantity()="+duration.getQuantity());
                if (duration.getUnit() instanceof TimeUnit){
                    result.add(duration);
                    numberUnitsShown = numberUnitsShown + 1;
                    logger.debug("added duration (inside while loop)");
                }
                if (duration.getUnit() instanceof Second){ break; }
            }
        }
        return result;
    }

    /**
     * Format the given {@link Duration} object, using the {@link TimeFormat}
     * specified by the {@link TimeUnit} contained within
     *
     * @param duration
     *            the {@link Duration} to be formatted
     * @return A formatted string representing {@code duration}
     */
    public String format(final Duration duration)
    {
        TimeFormat format = duration.getUnit().getFormat();
        return format.format(duration);
    }

    public String format(final List<Duration> durations)
    {
        TimeFormat format = new BasicTimeFormat().setPattern("%n %u").setPastSuffix(" ago").setFutureSuffix(" from now");
        return format.format(durations);
    }



    /**
     * Format the given {@link Date} object. This method applies the {@code
     * PrettyTime.approximateDuration(date)} method to perform its calculation
     *
     * @param then
     *            the {@link Date} to be formatted
     * @return A formatted string representing {@code then}
     */
    public String format(final Date then)
    {
        Duration d = approximateDuration(then);
        return format(d);
    }

    public String formatPrecise(final Date then)
    {
       List<Duration> durations = calculatePreciseDuration(then);
       return format(durations);
    }

    /**
     * Get the current reference timestamp.
     * <p>
     * See {@code PrettyTime.setReference(Date timestamp)}
     *
     * @return
     */
    public Date getReference()
    {
        return reference;
    }

    /**
     * Set the reference timestamp.
     * <p>
     * If the Date formatted is before the reference timestamp, the format
     * command will produce a String that is in the past tense. If the Date
     * formatted is after the reference timestamp, the format command will
     * produce a string that is in the future tense.
     *
     * @param timestamp
     */
    public void setReference(final Date timestamp){
        reference = TimeUtils.nowInGmtCalendar().getTime();
    }

    /**
     * Get a {@link List} of the current configured {@link TimeUnit}s in this
     * instance.
     *
     * @return
     */
    public List<TimeUnit> getUnits()
    {
        return units;
    }

    /**
     * Set the current configured {@link TimeUnit}s to be used in calculations
     *
     * @return
     */
    public void setUnits(final List<TimeUnit> units)
    {
        this.units = units;
    }

    public void setUnits(final TimeUnit... units)
    {
        this.units = Arrays.asList(units);
    }



}
