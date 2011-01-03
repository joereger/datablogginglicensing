/*
 * PrettyTime is an OpenSource Java time comparison library for creating human
 * readable time.
 * 
 * Copyright (C) 2009 - Lincoln Baxter, III <lincoln@ocpsoft.com>
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see the file COPYING.LESSER3 or visit the
 * GNU website at <http://www.gnu.org/licenses/>.
 */
package reger.core.agotext.units;

import reger.core.agotext.TimeUnit;
import reger.core.agotext.TimeFormat;
import reger.core.agotext.BasicTimeFormat;

public class Hour implements TimeUnit
{
    private static final TimeFormat defaultFormat = new BasicTimeFormat().setPattern("%n %u").setPastSuffix(" ago")
            .setFutureSuffix(" from now");

    private TimeFormat format = defaultFormat;
    private long maxQuantity = 0;
    private final long millisPerUnit = 1000 * 60 * 60;
    private String name = "hour";
    private String pluralName = "hours";

    public TimeFormat getFormat()
    {
        return format;
    }

    public long getMaxQuantity()
    {
        return maxQuantity;
    }

    public long getMillisPerUnit()
    {
        return millisPerUnit;
    }

    public String getName()
    {
        return name;
    }

    public String getPluralName()
    {
        return pluralName;
    }

    public void setFormat(final TimeFormat format)
    {
        this.format = format;
    }

    public void setMaxQuantity(final long maxQuantity)
    {
        this.maxQuantity = maxQuantity;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public void setPluralName(final String pluralName)
    {
        this.pluralName = pluralName;
    }
}
