/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.charts;

import net.sf.xplanner.domain.Iteration;

import com.technoetic.xplanner.util.TimeGenerator;

/**
 * User: Mateusz Prokopowicz
 * Date: Apr 19, 2005
 * Time: 5:29:45 PM
 */
public interface DataSampler
{
   void generateOpeningDataSamples(Iteration iteration);

   void generateClosingDataSamples(Iteration iteration);

   void setTimeGenerator(TimeGenerator timeGenerator);

   void generateDataSamples(Iteration iteration);
}
