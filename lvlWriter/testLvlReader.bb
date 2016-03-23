Include "lvlWriter.bb"

LoadLevel()


Function LoadLevel(f_name$="default.lvl")
    file=OpenFile(f_name$)
	lvlSetFile(file)
    If lvlReadChunk$() = "VOID"
    	Print "Version: "+lvlReadInt()
		Print "Date Created: "+lvlReadString$() 
		Print "Time Created: "+lvlReadString$()
		Print "Description: "+lvlReadString$()		
    End If
    lvlExitChunk() 


   If lvlReadChunk$() = "EMBD"
		If lvlReadChunk$() = "MUSC"
			music_name$=lvlReadString$()
			If lvlReadChunk$() = "DATA"
			    music_size = lvlChunkSize()
			    music_file=WriteFile(music_name$)
				For pos = 1 To music_size
					WriteByte(music_file,lvlReadByte())
				Next 
				CloseFile(music_file)
				PlayMusic(music_name$)
				DeleteFile(music_name$)
			End If	
			lvlExitChunk()
		End If	
		lvlExitChunk()
		If lvlReadChunk$() ="IMGS"
			image_name$ = lvlReadString$()
			If lvlReadChunk$() = "DATA"
				image_size = lvlChunkSize()
				image_file = WriteFile(image_name$)
				For pos = 1 To image_size
					WriteByte(image_file,lvlReadByte())
				Next
				CloseFile(image_file)
				image = LoadImage(image_name$)
				DeleteFile(image_name$)
			End If		
			lvlEndChunk()
		End If	
		lvlEndChunk()	
	End If	
   lvlExitChunk()
CloseFile(file)
Print "Music name: "+ music_name$
Print "Music size "+ music_size
Print "Image name: "+ image_name$
Print "Image size "+ image_size
DrawImage image,100,100

End Function

WaitKey()