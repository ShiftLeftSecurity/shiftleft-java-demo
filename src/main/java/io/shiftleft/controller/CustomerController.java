package io.shiftleft.controller;

@RequestMapping(value = "/saveSettings", method = RequestMethod.GET)
  public void saveSettings(HttpServletResponse httpResponse, WebRequest request) throws Exception {
    // "Settings" will be stored in a cookie
    // schema: base64(filename,value1,value2...), md5sum(base64(filename,value1,value2...))

    if (!checkCookie(request)){
      httpResponse.getOutputStream().println("Error");
      throw new Exception("cookie is incorrect");
    }

    String settingsCookie = request.getHeader("Cookie");
    String[] cookie = settingsCookie.split(",");
if(cookie.length<2) {
  httpResponse.getOutputStream().println("Malformed cookie");
      throw new Exception("cookie is incorrect");
    }

    String base64txt = cookie[0].replace("settings=","");

    // Check md5sum
    String cookieMD5sum = cookie[1];
    String calcMD5Sum = DigestUtils.md5Hex(base64txt);
if(!cookieMD5sum.equals(calcMD5Sum))
    {
      httpResponse.getOutputStream().println("Wrong md5");
      throw new Exception("Invalid MD5");
    }

    // Now we can store on filesystem
    String[] settings = new String(Base64.getDecoder().decode(base64txt)).split(",");
// storage will have ClassPathResource as basepath
    ClassPathResource cpr = new ClassPathResource("./static/");

    // Whitelist the filename to prevent directory traversal
    String filename = settings[0];
    if(!filename.matches("[a-zA-Z0-9._-]+")) {
      httpResponse.getOutputStream().println("Invalid filename");
      throw new Exception("Filename contains invalid characters");
    }

    File file = new File(cpr.getPath()+filename);
    if(!file.exists()) {
      file.getParentFile().mkdirs();
    }

    FileOutputStream fos = new FileOutputStream(file, true);
    // First entry is the filename -> remove it
    String[] settingsArr = Arrays.copyOfRange(settings, 1, settings.length);
    // on setting at a linez
    fos.write(String.join("\n",settingsArr).getBytes());
    fos.write(("\n"+cookie[cookie.length-1]).getBytes());
    fos.close();
    httpResponse.getOutputStream().println("Settings Saved");
  }
