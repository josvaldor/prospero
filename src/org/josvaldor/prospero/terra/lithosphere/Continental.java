package org.josvaldor.prospero.terra.lithosphere;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.josvaldor.prospero.terra.unit.Coordinate;

/**
 * Class HgtReader reads data from SRTM HGT files. Currently this class is
 * restricted to a resolution of 3 arc seconds.
 *
 * SRTM data files are available at the
 * <a href="http://dds.cr.usgs.gov/srtm/version2_1/SRTM3">NASA SRTM site</a>
 * 
 * @author Oliver Wieland &lt;oliver.wieland@online.de&gt;
 */
public class Continental {
	public static void main(String[] args) {
		Continental e = new Continental();
		System.out.println(e.getElevationFromHgt(41.5, -114));
	}

	private static final int SECONDS_PER_MINUTE = 60;
	public static final String HGT_EXT = ".hgt";
	public static final int HGT_RES = 3; // resolution in arc seconds
	public static final int HGT_ROW_LENGTH = 1201; // number of elevation values
													// per line
	public static final int HGT_VOID = -32768; // magic number which indicates
												// 'void data' in HGT file

	private final HashMap<String, ShortBuffer> cache = new HashMap<String, ShortBuffer>();
	
	public List<Coordinate> box(double latA, double lonA, double latB, double lonB) {
		List<Coordinate> cList = new LinkedList<Coordinate>();
		Coordinate c = null;
		double increment = 1.0;
		Double elevation = null;
		for(double i=latA;i<latB;i+=increment) {
			for(double j=lonA;j<lonB;j+=increment) {
				elevation = this.getElevationFromHgt(i, j);
				if(elevation != null) {
					c = new Coordinate();
					c.latitude = i;
					c.longitude = j;
					c.elevation = elevation;
					System.out.println(c);
					cList.add(c);
				}
			}
		}
		return cList;
	}

	public Double getElevationFromHgt(double latitude, double longitude) {
		Double elevation = null;
		try {
			String file = getHgtFileName(latitude, longitude);
			String fullPath = new File("./data/lithosphere/continental/", file).getPath();
			ShortBuffer data = readHgtFile(fullPath);
			this.cache.put(getHgtFileName(latitude, longitude), data);
			elevation = readElevation(latitude, longitude);
		} catch (FileNotFoundException e) {
//			 System.err.println("Get elevation from HGT " + latitude + " " + longitude + " failed: => " + e.getMessage());
		} catch (Exception ioe) {
			ioe.printStackTrace(System.err);
		}
		return elevation;
	}

	ByteBuffer bb;
	@SuppressWarnings("resource")
	private ShortBuffer readHgtFile(String file) throws Exception {
		// CheckParameterUtil.ensureParameterNotNull(file);
		FileChannel fc = null;
		ShortBuffer sb = null;
		try {
			// Eclipse complains here about resource leak on 'fc' - even with
			// 'finally' clause???
			fc = new FileInputStream(file).getChannel();
			// choose the right endianness
//			System.out.println((int)fc.size());
			if(fc.size()>0){
				bb = ByteBuffer.allocateDirect((int)fc.size());
				while (bb.remaining() > 0)
					fc.read(bb);
				bb.flip();
				// sb = bb.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
				sb = bb.order(ByteOrder.BIG_ENDIAN).asShortBuffer();
				
			}
			fc.close();
		} 
//		catch (OutOfMemoryError E) {
//		    // release some (all) of the above objects
//		}
		finally {
			if (fc != null)
				fc.close();
		}

		return sb;
	}

	/**
	 * Reads the elevation value for the given coordinate.
	 *
	 * See also <a href=
	 * "http://gis.stackexchange.com/questions/43743/how-to-extract-elevation-from-hgt-file">stackexchange.com</a>
	 * 
	 * @param coor
	 *            the coordinate to get the elevation data for
	 * @return the elevation value or <code>Double.NaN</code>, if no value is
	 *         present
	 */
	public double readElevation(double latitude, double longitude) {
		String tag = getHgtFileName(latitude, longitude);
		short ele = 0;
		ShortBuffer sb = cache.get(tag);

		if (sb == null) {
			// return ElevationHelper.NO_ELEVATION;
//			System.out.println("No elevation");
			return 0;
		}

		// see
		// http://gis.stackexchange.com/questions/43743/how-to-extract-elevation-from-hgt-file
		double fLat = frac(latitude) * SECONDS_PER_MINUTE;
		double fLon = frac(longitude) * SECONDS_PER_MINUTE;

		// compute offset within HGT file
		int row = (int) Math.round(fLat * SECONDS_PER_MINUTE / HGT_RES);
		int col = (int) Math.round(fLon * SECONDS_PER_MINUTE / HGT_RES);

		row = HGT_ROW_LENGTH - row;
		int cell = (HGT_ROW_LENGTH * (row - 1)) + col;

//		System.out.println(
//				"Read SRTM elevation data from row/col/cell " + row + "," + col + ", " + cell + ", " + sb.limit());

		// valid position in buffer?
		if (cell < sb.limit()) {
			ele = sb.get(cell);
			// System.out.println("==> Read SRTM elevation data from
			// row/col/cell " + row + "," + col + ", " + cell + " = " + ele);
			// check for data voids
			if (ele == HGT_VOID) {
				// return ElevationHelper.NO_ELEVATION;
			} else {
				return ele;
			}
		} else {
			// return ElevationHelper.NO_ELEVATION;
		}
		return ele;
	}

	/**
	 * Gets the associated HGT file name for the given way point. Usually the
	 * format is <tt>[N|S]nn[W|E]mmm.hgt</tt> where <i>nn</i> is the integral
	 * latitude without decimals and <i>mmm</i> is the longitude.
	 *
	 * @param latLon
	 *            the coordinate to get the filename for
	 * @return the file name of the HGT file
	 */
	public String getHgtFileName(double latitude, double longitude) {
		int lat = (int) latitude;
		int lon = (int) longitude;

		String latPref = "N";
		if (lat < 0)
			latPref = "S";

		String lonPref = "E";
		if (lon < 0) {
			lonPref = "W";
		}

		return String.format("%s%02d%s%03d%s", latPref, (lat < 0) ? -lat : lat, lonPref, (lon < 0) ? -lon : lon,
				HGT_EXT);
	}

	public static double frac(double d) {
		long iPart;
		double fPart;

		// Get user input
		iPart = (long) d;
		fPart = d - iPart;
		return fPart;
	}
}
