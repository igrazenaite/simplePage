package simplePage.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import simplePage.item.AddItem;
import simplePage.item.Item;
import simplePage.item.ItemForClient;
import simplePage.owner.Owner;
import simplePage.repositories.ItemRepository;
import simplePage.repositories.OwnerRepository;
import simplePage.repositories.StorageRepository;
import simplePage.storage.Storage;

@Transactional
@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private StorageRepository storageRepository;

	public OwnerRepository getOwnerRepository() {
		return ownerRepository;
	}

	public void setOwnerRepository(OwnerRepository ownerRepository) {
		this.ownerRepository = ownerRepository;
	}

	public StorageRepository getStorageRepository() {
		return storageRepository;
	}

	public void setStorageRepository(StorageRepository storageRepository) {
		this.storageRepository = storageRepository;
	}

	public ItemRepository getItemRepository() {
		return itemRepository;
	}

	public void setItemRepository(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	public List<ItemForClient> receiveAllItems() {
		List<Item> itemsFromDatabase = getItemRepository().findAll();
		List<ItemForClient> itemsForClient = itemsFromDatabase.stream().map((item) -> {
			ItemForClient itm = new ItemForClient();
			itm.setItemId(item.getItemId());
			itm.setItemName(item.getItemName());
			itm.setQuantity(item.getQuantity());
			itm.setOwnerId(item.getOwner().getOwnerId());
			itm.setStorageId(item.getStorageFacility().getStorageId());
			return itm;
		}).collect(Collectors.toList());
		return itemsForClient;
	}

	public ItemForClient receiveItemInfo(long itemId) {
		Item item = itemRepository.findByItemId(itemId);
		ItemForClient itm = new ItemForClient();
		itm.setItemId(item.getItemId());
		itm.setItemName(item.getItemName());
		itm.setQuantity(item.getQuantity());
		itm.setOwnerId(item.getOwner().getOwnerId());
		itm.setStorageId(item.getStorageFacility().getStorageId());
		return itm;
	}

	public void addNewItem(AddItem newItem, long ownerId, long storageId) {
		Item item = new Item();
		item.setItemName(newItem.getItemName());
		item.setQuantity(newItem.getQuantity());
		Owner owner = ownerRepository.findByOwnerId(ownerId);
		item.setOwner(owner);
		Storage storageFacility = storageRepository.findByStorageId(storageId);
		item.setStorageFacility(storageFacility);
		itemRepository.save(item);
	}

	public void updateItem(Item item, Long itemId, long ownerId, long storageId) {
		Item itm = itemRepository.findOne(itemId);
		itm.setItemName(item.getItemName());
		itm.setQuantity(item.getQuantity());
		Owner owner = ownerRepository.findByOwnerId(ownerId);
		itm.setOwner(owner);
		Storage storageFacility = storageRepository.findByStorageId(storageId);
		itm.setStorageFacility(storageFacility);
		itemRepository.save(itm);
	}

	public void deleteItem(Long itemId) {
		itemRepository.delete(itemId);
	}
}
