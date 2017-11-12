/**
     * Retrieves Uri from Bitmap.
     * @param mContext context
     * @param mImage Bitmap image
     * @return
     */
    public static Uri getUriFromBitmap(Context mContext, Bitmap mImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        mImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), mImage, null, null);
        return Uri.parse(path);
    }
