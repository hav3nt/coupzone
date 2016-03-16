package com.rest.resources;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.core.exceptions.CouponSystemException;
import com.core.facades.ClientFacade;
import com.core.facades.CompanyFacade;
import com.sun.jersey.multipart.FormDataParam;

/**
 * a class that manages all of the coupon image uploads. every http request that
 * uploads an image will be sent to:
 * 'http://localhost:8080/MyProjectName/rest/upload/coupon/image'
 */
@Path("/upload/coupon/image")
public class FileUploadManager {


	/**
	 * the method that receives the http request that's being sent with the
	 * image
	 * 
	 * @param fileData
	 *            = the data of the file that's being sent - the coupon image
	 * @param fileDetail
	 *            = the details describing the file (image) such as the files
	 *            name
	 * @param req
	 *            = the http request that has been sent
	 * @return the name of the image file which was uploaded
	 */
	@POST
	@Produces({ MediaType.TEXT_PLAIN })
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	public String uploadFiles(@FormDataParam("file") InputStream fileData,
			@Context HttpServletRequest request, @Context ServletConfig servletConfig)
					throws CouponSystemException {
		String fileName=String.valueOf(new Date().getTime())+".jpg";
		CompanyFacade companyFacade=getFacadeFromSession(request);
		// adds the id of the company user at beginning of image file name
		if(companyFacade!=null){
			fileName=companyFacade.getCompID()+fileName;
		}
		// keeps the path to the images folder inside the server
		String path = getImageDirOnServer(servletConfig);
		path = path.replace('\\', '/');
		
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			byte[] buffer = new byte[1024];
			int len;
			while ((len = fileData.read(buffer)) > -1 ) {
			    baos.write(buffer, 0, len);
			}
			baos.flush();
			
			// creating 2 input streams to user to write the image to the server and to the HD
			InputStream is1 = new ByteArrayInputStream(baos.toByteArray()); 
			InputStream is2 = new ByteArrayInputStream(baos.toByteArray()); 
			writeToDisk(is1, path+"/" + fileName);
			path=getImageDirOnHD(servletConfig);
			writeToDisk(is2, path+"/" + fileName);
		} catch (IOException e) {
			throw new CouponSystemException("Failed uploading image",e);
		}

		return fileName;
	}

	
	/**
	 * loades an image file to the server, if the image file found on HD and not found 
	 * on the server already
	 * @param servletConfig - the servlet configuration object to use for initial parameters
	 * @param String - the name of the searched image file
	 * @return a string "True" or "False" whether the image file is on server
	 * */
	@GET
	@Path("/check/{name}")
	@Produces({ MediaType.TEXT_PLAIN })
	@Consumes(MediaType.APPLICATION_JSON)
	public String isLoadedImageOnServer(@Context ServletConfig servletConfig, @PathParam("name") String imageName) {
		File imageFileOnServer = new File(this.getImageDirOnServer(servletConfig) + "/" + imageName);
		
		// return the string answer to whether the image file exists on server
		return String.valueOf(imageFileOnServer.exists());
	}

	
	@GET
	@Path("/load/{name}")
	@Produces({ MediaType.TEXT_PLAIN })
	@Consumes(MediaType.APPLICATION_JSON)
	public String loadImageOnServer(@Context ServletConfig servletConfig, @PathParam("name") String imageName) throws FileNotFoundException, IOException {
		File imageFileOnHD = new File(this.getImageDirOnHD(servletConfig) + "/" + imageName);
		Files.copy(imageFileOnHD.toPath(),new FileOutputStream(this.getImageDirOnServer(servletConfig) + "/" + imageName));
		
		return "true";
	}
	
	@DELETE
	@Path("/delete/{name}")
	@Produces({ MediaType.TEXT_PLAIN })
	@Consumes(MediaType.APPLICATION_JSON)
	public String deleteImage(@Context ServletConfig servletConfig, @PathParam("name") String imageName){
		File imageFileOnServer = new File(this.getImageDirOnServer(servletConfig) + "/" + imageName);
		File imageFileOnHD = new File(this.getImageDirOnHD(servletConfig) + "/" + imageName);
		imageFileOnServer.delete();
		imageFileOnHD.delete();
		return imageName;
	}
	
	
	
	/**
	 * a method the writes the uploaded image to the server
	 * @param uploadedInputStream
	 *            = the file's data - that will be written to the disk
	 * @param uploadedFileLoaction
	 *            = the location to which the file will be written
	 */
	private void writeToDisk(InputStream uploadedInputStream, String uploadedFileLocation)
			throws CouponSystemException {
		try (OutputStream out = new FileOutputStream(uploadedFileLocation)) {
			int read = 0;
			byte[] bytes = new byte[1024];
			// a loop that copies every byte from the fileData
			// (uploadedInputStream) and writes to the path we declared
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
		} catch (IOException e) {
			throw new CouponSystemException("problem uploading image");
		}
	}
	
	
	
	/**
	 * returns the path of the coupon images folder on server,
	 * if folder dorsn't exist, creates it
	 * @param ServletConfig - the servlet configuration object to get initial parameter 
	 * @return the path to the images folder on server
	 * */
	private String getImageDirOnServer(@Context ServletConfig servletConfig) {
		ServletContext servletContext=servletConfig.getServletContext();
		String dirName = "/" + servletConfig.getInitParameter("serverImagesDirName");
		
		// gets the real path of the actual folder on the server 
		String dirPath = servletContext.getRealPath(dirName);
		dirPath = dirPath.replace('\\', '/');
		File dir = new File(dirPath);
		
		// checks if the folder exists, if doesn't exist creates the folder
		if (!dir.exists()) {
			dir.mkdir();
		}
		return dirPath;
	}
	
	/**
	 * returns the path of the coupon images folder on the HardDisk,
	 * if folder dorsn't exist, creates it
	 * @param ServletConfig - the servlet configuration object to get initial parameter 
	 * @return the path to the images folder on HD
	 * */
	private String getImageDirOnHD(@Context ServletConfig servletConfig) {
		// keeps the initial parameter value
		// in our case 'C:/couponPictures'
		String dirPath = servletConfig.getInitParameter("HDImagesDirName");
		File dir = new File(dirPath);
		// checks if the folder exists, if doesn't exist creates the folder
		if (!dir.exists()) {
			dir.mkdir();
		}
		return dirPath;
	}
	
	
	/**
	 * returns the facade attribute object from the session
	 * 
	 * @throws CouponSystemException
	 *             - if the facade object is not an instance of CompanyFacade
	 */
	private CompanyFacade getFacadeFromSession(HttpServletRequest request) throws CouponSystemException {
		HttpSession session = request.getSession(false);
		ClientFacade facade = (ClientFacade) session.getAttribute("facade");
		if (facade instanceof CompanyFacade) {
			return (CompanyFacade) facade;
		}
		return null;
	}
}
