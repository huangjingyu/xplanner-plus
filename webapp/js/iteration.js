$(function() {
	//Drag & drop ordering applicable only
	//when sorted by Order ascending and user has edit permission.
	
	orderHeaderCell = $('.sortable:nth-child(4)');

	isSortedByOrderAscending = (orderHeaderCell.hasClass('order1') &&
			orderHeaderCell.hasClass('sorted'));
	isOrderEditable = $('#objecttable input:text').length > 0;
	
	if(isSortedByOrderAscending && isOrderEditable) {
		$('#objecttable > tbody').sortable({
			update: function(event, ui) {
				//Renumber order texts
				$('#objecttable input:text').each(function(n) {
					this.value = n + 1;
				});

				//Update stripes
				$('#objecttable > tbody > tr').removeClass('odd even').filter(':even').addClass('odd');
				$('#objecttable > tbody > tr').filter(':odd').addClass('even');
			}
		});
	}
});

function submitMoveMultiple(action)
{
	var numChecked = $('#objecttable input:checked').length;
	if(numChecked > 0)
	{
		$("form[name='reordering']").attr("action", action).submit();
	}
}