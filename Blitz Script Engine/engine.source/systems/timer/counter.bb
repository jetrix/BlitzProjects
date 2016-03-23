Global currentsec
StartTimer()
Print "counting up! to 8"
While Not KeyHit(1)


If GetTimer(1*10^3) = True Then Print "1"
If GetTimer(2*10^3) = True Then Print "2"
If GetTimer(3*10^3) = True Then Print "3"
If GetTimer(4*10^3) = True Then Print "4"
If GetTimer(5*10^3) = True Then Print "5"
If GetTimer(6*10^3) = True Then Print "6"



Wend


Function StartTimer()
currentsec=MilliSecs()
End Function

Function StopTimer()
currentsec=0
End Function

Function ResetTimer()
currentsec=MilliSecs()
End Function

Function GetTimer(milli)
If MilliSecs() = currentsec + milli Then Return True
End Function

