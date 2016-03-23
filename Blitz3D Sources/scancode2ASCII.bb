


For a = 1 To 88
DebugLog "Scancode: "+a+" ASCII "+ScanCode2ASCII(a)+" = " + Chr$(Scancode2ASCII(a))
Next
WaitKey()


Function Scancode2ASCII( key )
	Select key
	Case 1    :Return 27
	Case 2    :Return 49
	Case 2+29 :Return 33
	Case 2+54 :Return 33
	Case 3    :Return 50
	Case 3+29 :Return 64
	Case 3+54 :Return 64
	Case 4    :Return 51
	Case 4+29 :Return 35
	Case 4+54 :Return 35
	Case 5    :Return 52
	Case 5+29 :Return 36
	Case 5+54 :Return 36
	Case 6    :Return 53
	Case 6+29 :Return 37
	Case 6+54 :Return 37
	Case 7    :Return 54
	Case 7+29 :Return 94
	Case 7+54 :Return 94
	Case 8    :Return 55
	Case 8+29 :Return 38
	Case 8+54 :Return 38
	Case 9    :Return 56
	Case 9+29 :Return 42
	Case 9+54 :Return 42
	Case 10   :Return 57
	Case 10+29 :Return 
	Case 10+54 :Return
	Case 11    :Return 48
	Case 11+29 :Return 
	Case 11+54 :Return
	Case 12    :Return 45
	Case 12+29 :Return 
	Case 12+54 :Return
	Case 13    :Return 42
	Case 13+29 :Return 
	Case 13+54 :Return
	Case 14    :Return 8
	Case 15    :Return 9
	Case 16    :Return 113
	Case 16+29 :Return 81
	Case 16+54 :Return 81
	Case 17    :Return "W"
	Case 17+29 :Return 
	Case 17+54 :Return 
	Case 18    :Return "E"
	Case 18+29 :Return 
	Case 18+54 :Return
	Case 19    :Return "R"
	Case 19+29 :Return 
	Case 19+54 :Return
	Case 20    :Return "T"
	Case 20+29 :Return 
	Case 20+54 :Return
	Case 21    :Return "Y"
	Case 21+29 :Return 
	Case 21+54 :Return
	Case 22    :Return "U"
	Case 22+29 :Return 
	Case 22+54 :Return
	Case 23	   :Return "I"
	Case 23+29 :Return 
	Case 23+54 :Return
	Case 24    :Return "O"
	Case 24+29 :Return 
	Case 24+54 :Return
	Case 25    :Return "P"
	Case 25+29 :Return 
	Case 25+54 :Return
	Case 26    :Return "["
	Case 26+29 :Return 
	Case 26+54 :Return
	Case 27    :Return "]"
	Case 27+29 :Return 
	Case 27+54 :Return
	Case 28    :Return "Enter/Return"
	Case 29    :Return "Left CTRL"
	Case 30    :Return "A"
	Case 30+29 :Return 
	Case 30+54 :Return
	Case 31    :Return "S"
	Case 31+29 :Return 
	Case 31+54 :Return
	Case 32    :Return "D"
	Case 32+29 :Return 
	Case 32+54 :Return
	Case 33    :Return "F"
	Case 33+29 :Return 
	Case 33+54 :Return
	Case 34    :Return "G"
	Case 34+29 :Return 
	Case 34+54 :Return
	Case 35    :Return "H"
	Case 35+29 :Return 
	Case 35+54 :Return
	Case 36    :Return "J"
	Case 36+29 :Return 
	Case 36+54 :Return
	Case 37    :Return "K"
	Case 37+29 :Return 
	Case 37+54 :Return
	Case 38    :Return "L"
	Case 38+29 :Return 
	Case 38+54 :Return
	Case 39    :Return ";"
	Case 39+29 :Return 
	Case 39+54 :Return
	Case 40    :Return "'"
	Case 40+29 :Return 
	Case 40+54 :Return
	Case 41    :Return "`"
	Case 41+29 :Return 
	Case 41+54 :Return
	Case 42    :Return "Left Shift"
	Case 43    :Return "\"
	Case 43+29 :Return 
	Case 43+54 :Return
	Case 44    :Return "Z"
	Case 44+29 :Return 
	Case 44+54 :Return
	Case 45    :Return "X"
	Case 45+29 :Return 
	Case 45+54 :Return
	Case 46    :Return "C"
	Case 46+29 :Return 
	Case 46+54 :Return
	Case 47    :Return "V"
	Case 47+29 :Return 
	Case 47+54 :Return
	Case 48    :Return "B"
	Case 48+29 :Return 
	Case 48+54 :Return
	Case 49    :Return "N"
	Case 49+29 :Return 
	Case 49+54 :Return
	Case 50    :Return "M"
	Case 50+29 :Return 
	Case 50+54 :Return
	Case 51    :Return ","
	Case 51+29 :Return 
	Case 51+54 :Return
	Case 52    :Return "."
	Case 52+29 :Return 
	Case 52+54 :Return
	Case 53    :Return "/"
	Case 53+29 :Return 
	Case 53+54 :Return
	Case 54    :Return "Right Shift"
	Case 55    :Return "Keypad *"
	Case 56    :Return "Left Alt"
	Case 57    :Return "Space"
	Case 58    :Return "Caps Lock"
	Case 59    :Return "F1"
	Case 60    :Return "F2"
	Case 61    :Return "F3"
	Case 62    :Return "F4"
	Case 63    :Return "F5"
	Case 64    :Return "F6"
	Case 65    :Return "F7"
	Case 66    :Return "F8"
	Case 67    :Return "F9"
	Case 68    :Return "F10"
	Case 69    :Return "Num Lock"
	Case 70    :Return "Scroll Lock"
	Case 71    :Return "Numpad 7"
	Case 72    :Return "Numpad 8"
	Case 73    :Return "Numpad 9"
	Case 74    :Return "Numpad -"
	Case 75    :Return "Numpad 4"
	Case 76    :Return "Numpad 5"
	Case 77    :Return "Numpad 6"
	Case 78    :Return "Numpad +"
	Case 79    :Return "Numpad 1"
	Case 80    :Return "Numpad 2"
	Case 81    :Return "Numpad 3"
	Case 82    :Return "Numpad 0"
	Case 83    :Return "Numpad ."
	Case 84    :Return "?"
	Case 85    :Return "?"
	Case 86    :Return "OEM 102"
	Case 87    :Return "F11"
	Case 88    :Return "F12"
End Select
End Function 