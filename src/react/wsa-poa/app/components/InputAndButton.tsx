import {ChangeEvent, useState} from "react";

function InputAndButton() {
  const [inputValue, setInputValue] = useState("");
  const [buttonValue, setButtonValue] = useState("");

  const handleInputChange = (e: ChangeEvent<HTMLInputElement>) => {
    setInputValue(e.target.value);
  };

  const handleButtonClick = () => {
    setButtonValue(inputValue);
  };

  return (
    <div>
      <input value={inputValue} onChange={handleInputChange} />
      <button onClick={handleButtonClick}>Click me</button>
      <p>{buttonValue}</p>
    </div>
  );
}

export default InputAndButton;