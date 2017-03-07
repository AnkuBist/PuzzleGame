package com.hgil.game.puzzle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Reads an image into a bitmap, downscales it to the
 * {@link #setMaxFrameDimension(int) frame dimension},
 * and sends the parent a {@link Message} containing this
 * object. This class is designed to run as a background task.
 */
public class ImageConverter implements Runnable
{
  @Override
  public void run()
  {
   InputStream pictureData = null;
   try
   {
    BitmapFactory.Options bitmapInfo = prepareOptions();
    pictureData = openInputStream();
    bitmap = BitmapFactory.decodeStream(pictureData, null, bitmapInfo);
   }
   catch (Throwable failure)
   {
    status = failure;
   }
   finally
   {
    if (null != pictureData)
     try { pictureData.close(); } catch (Exception ignored) {}
    notifyParent();
   }
  }

  protected void notifyParent()
  {
   if (null == message)
    message = new Message();
   message.obj = this;
   parentHandler.sendMessage(message);
  }

  protected BitmapFactory.Options prepareOptions() throws IOException
  {
   InputStream pictureData = null;
   try
   {
    pictureData = openInputStream();
    BitmapFactory.Options bitmapInfo = new  BitmapFactory.Options();
    bitmapInfo.inJustDecodeBounds = true;
    BitmapFactory.decodeStream(pictureData, null, bitmapInfo);
    pictureData.close();
    pictureData = null;
    int maxBitmapSize = bitmapInfo.outWidth;
    if (maxBitmapSize < bitmapInfo.outHeight)
     maxBitmapSize = bitmapInfo.outHeight;
    if (maxBitmapSize > maxFrameDim && (0 > maxFrameDim * 2 || maxBitmapSize >= maxFrameDim * 2))
    {
     bitmapInfo.inJustDecodeBounds = false;
     bitmapInfo.inSampleSize = (maxBitmapSize - 1) / maxFrameDim + 1;
     return bitmapInfo;
    }
    else
     return null;
   }
   finally
   {
    if (null != pictureData)
     try { pictureData.close(); } catch (Exception ignored) {}
   }
  }
  
  protected InputStream openInputStream() throws FileNotFoundException
  {
   if (null != imageURI)
    return context.getContentResolver().openInputStream(imageURI);
   else
    return context.getResources().openRawResource(imageResource);
  }

  public Throwable getStatus()
  {
   return status;
  }

  public Bitmap getBitmap()
  {
   return bitmap;
  }

  public void setMaxFrameDimension(int dim)
  {
   this.maxFrameDim = dim;
  }

  public Uri getImageURI()
  {
   return imageURI;
  }

  public void setImageURI(Uri imageURI)
  {
   this.imageURI = imageURI;
   this.imageResource = 0;
  }

  public void setImageId(Serializable imageId)
  {
   if (imageId instanceof Integer)
   {
    this.imageResource = (Integer)imageId;
    this.imageURI = null;
   }
   else
    setImageURI(Uri.fromFile(ImageSource.imageFileForId((String)imageId, context)));
  }

  /**
   * Stores a predefined message to be sent with the notification.
   * Note that the {@link Message#obj} property of the argument will
   * be replaced with this object's reference. 
   * @param message predefined message object
   */
  public void setMessage(Message message)
  {
   this.message = message;
  }

  public ImageConverter(final Context context, final Handler handler)
  {
   this.parentHandler = handler;
   this.context = context;
  }

  protected void setStatus(Throwable status)
  {
   this.status = status;
  }

  protected Handler getParentHandler()
  {
   return parentHandler;
  }

  private Context context;
  private Handler parentHandler;
  private Throwable status;
  private Uri imageURI;
  private int imageResource;
  private int maxFrameDim;
  private Bitmap bitmap;
  private Message message;
}