using DellKvm.Core;
using DellKvm.Protocol;

namespace DellKvm.WinForms;

public sealed class MainForm : Form
{
    private readonly TextBox _hostTextBox = new();
    private readonly TextBox _portTextBox = new();
    private readonly TextBox _tokenTextBox = new();
    private readonly Button _connectButton = new();
    private readonly Label _statusLabel = new();

    private KvmTcpConnection? _connection;

    public MainForm()
    {
        Text = "Dell KVM";
        StartPosition = FormStartPosition.CenterScreen;
        MinimumSize = new Size(560, 260);
        Size = new Size(680, 310);

        BuildInterface();

        FormClosed += async (_, _) => await DisconnectAsync();
    }

    private void BuildInterface()
    {
        var layout = new TableLayoutPanel
        {
            Dock = DockStyle.Fill,
            Padding = new Padding(12),
            ColumnCount = 2,
            RowCount = 5,
            AutoSize = false
        };

        layout.ColumnStyles.Add(
            new ColumnStyle(SizeType.Absolute, 145));

        layout.ColumnStyles.Add(
            new ColumnStyle(SizeType.Percent, 100));

        AddRow(layout, 0, "BMC host or IP:", _hostTextBox);
        AddRow(layout, 1, "KVM TCP port:", _portTextBox);
        AddRow(layout, 2, "Session token:", _tokenTextBox);

        _tokenTextBox.UseSystemPasswordChar = true;

        _connectButton.Text = "Connect";
        _connectButton.AutoSize = true;
        _connectButton.Click += ConnectButton_Click;

        layout.Controls.Add(_connectButton, 1, 3);

        _statusLabel.Text = "Not connected";
        _statusLabel.AutoSize = true;
        _statusLabel.Padding = new Padding(0, 8, 0, 0);

        layout.Controls.Add(_statusLabel, 0, 4);
        layout.SetColumnSpan(_statusLabel, 2);

        Controls.Add(layout);
    }

    private static void AddRow(
        TableLayoutPanel layout,
        int row,
        string caption,
        Control input)
    {
        var label = new Label
        {
            Text = caption,
            AutoSize = true,
            Anchor = AnchorStyles.Left,
            Margin = new Padding(3, 8, 3, 8)
        };

        input.Dock = DockStyle.Fill;
        input.Margin = new Padding(3, 5, 3, 5);

        layout.Controls.Add(label, 0, row);
        layout.Controls.Add(input, 1, row);
    }

    private async void ConnectButton_Click(
        object? sender,
        EventArgs e)
    {
        if (_connection is not null)
        {
            await DisconnectAsync();
            return;
        }

        if (!int.TryParse(_portTextBox.Text, out var port))
        {
            MessageBox.Show(
                this,
                "Enter a valid TCP port.",
                "Invalid port",
                MessageBoxButtons.OK,
                MessageBoxIcon.Warning);

            return;
        }

        var options = new KvmConnectionOptions
        {
            Host = _hostTextBox.Text.Trim(),
            Port = port,
            SessionToken = _tokenTextBox.Text
        };

        try
        {
            options.Validate();

            _connectButton.Enabled = false;
            _statusLabel.Text = "Connecting...";

            var connection = await KvmTcpConnection.ConnectAsync(
                options.Host,
                options.Port);

            try
            {
                var request =
                    VideoSessionProtocol.CreateValidationRequest(
                        options.SessionToken);

                await connection.WritePacketAsync(request);
                _connection = connection;
            }
            catch
            {
                await connection.DisposeAsync();
                throw;
            }

            _statusLabel.Text =
                "TCP connected; validation request sent. " +
                "Response handling is not implemented yet.";

            _connectButton.Text = "Disconnect";
        }
        catch (Exception ex)
        {
            _statusLabel.Text = "Connection failed";

            MessageBox.Show(
                this,
                ex.Message,
                "Connection error",
                MessageBoxButtons.OK,
                MessageBoxIcon.Error);
        }
        finally
        {
            _connectButton.Enabled = true;
        }
    }

    private async Task DisconnectAsync()
    {
        var connection = _connection;
        _connection = null;

        if (connection is not null)
        {
            await connection.DisposeAsync();
        }

        if (!IsDisposed)
        {
            _connectButton.Text = "Connect";
            _statusLabel.Text = "Not connected";
        }
    }
}
