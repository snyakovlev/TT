package ru.television.online;

public class Channel {

        private String id;
        private String idd;
        private String name;
        private String file;
        private String file2;
        private String file3;
        private String image=null;
        private String group;
        private String select;




        public String getName() {
            return name;
        }

        public void setName(String name)
        {
            this.name=name;
        }


    public String getImage() {
        return image;
    }

    public void setImage(String image)
    {
        this.image=image;
    }

        public String getId() {
            return id;
        }

        public void setId(String id)
        {
            this.id=id;
        }




        public String getIdd() {
            return idd;
        }

    public void setIdd(String idd)
    {
        this.idd=idd;
    }



        public String getFile() {
            return file;
        }

    public void setUrlImage(String file)
    {
        this.file=file;
    }


    public String getFile2() {
        return file2;
    }

    public void setUrlImage2(String file)
    {
        this.file2=file;
    }


    public String getFile3() {
        return file3;
    }

    public void setUrlImage3(String file)
    {
        this.file3=file;
    }


    public String getGroup() {
        return group;
    }

    public void setGroup(String file)
    {
        this.group=file;
    }


    public String getSelect() {
        return select;
    }

    public void setSelect(String select)
    {
        this.select=select;
    }



}
 class ItemProgram
{
    String start_time;
    String stop_time;
    long start_millisec;
    long stop_millisec;
    String text;
    String desc;
}
