
document.write(
`
<div class='webpage' style='margin-top:5%;'>
  <h1> Account Settings </h1>
  <hr/>
  <h3> Change Password</h3>
  <div style='display:inline-block;'>
    <form action='../change_password.php' method='post'>
      <table>
        <tr>
          <th>current password: </th><td><input type='password' name='password_old'></td>
        </tr>
        <tr>
          <th>new password: </th><td><input type='password' name='password_new'></td>
        </tr>
        <tr>
          <th>confirm new password:</th><td><input type='password' name='password_new_confirm'></td>
        </tr>
      </table>
      <input type='submit' name='submit' value='change password'>
    </form>
  </div>
</div>
`);
